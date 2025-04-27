package com.dungnguyen.cloudgateway.filter;

import com.dungnguyen.cloudgateway.config.RouteRoleConfig;
import com.dungnguyen.cloudgateway.model.dto.AuthorizationResponseDTO;
import com.dungnguyen.cloudgateway.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class JwtFilterToken implements WebFilter {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Set<String>> routeRoles;

    // Constructor with explicit injection of routeRoles bean
    public JwtFilterToken(WebClient.Builder webClientBuilder, Map<String, Set<String>> routeRoles) {
        this.webClientBuilder = webClientBuilder;
        this.routeRoles = routeRoles;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Check if the path is public and should bypass token check
        if (RouteRoleConfig.isPublicRoute(path, routeRoles)) {
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return handleError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }

        String token = authorization.substring(7);

        return validateToken(token)
                .flatMap(apiResponse -> {
                    if (apiResponse != null && apiResponse.getData() != null) {
                        // Extract user data and role from the token
                        AuthorizationResponseDTO userData = apiResponse.getData();
                        exchange.getAttributes().put("userId", userData.getUserId());
                        exchange.getAttributes().put("email", userData.getEmail());

                        // Extract role from the token validation response
                        String userRole = apiResponse.getData().getRole();

                        // Check if user has access to the requested resource
                        if (!RouteRoleConfig.isRouteAccessibleByRole(path, userRole, routeRoles)) {
                            return handleError(exchange, HttpStatus.FORBIDDEN,
                                    "Access denied. Your role does not have permission to access this resource.");
                        }

                        // Token is valid and user has access, proceed with the request
                        return chain.filter(exchange);
                    } else {
                        // Token validation failed but we got a response
                        return handleError(exchange, HttpStatus.UNAUTHORIZED,
                                apiResponse != null ? apiResponse.getMessage() : "Token validation failed");
                    }
                })
                .onErrorResume(e -> {
                    log.error("Token validation error: {}", e.getMessage());
                    String errorMessage = "An unexpected error occurred during authentication";

                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException wcre = (WebClientResponseException) e;
                        HttpStatus status = HttpStatus.valueOf(wcre.getStatusCode().value());

                        try {
                            // Try to parse error response from auth service
                            ApiResponse<?> apiResponse = objectMapper.readValue(
                                    wcre.getResponseBodyAsString(),
                                    new com.fasterxml.jackson.core.type.TypeReference<ApiResponse<?>>() {});

                            errorMessage = apiResponse.getMessage();
                            return handleError(exchange, status, errorMessage);
                        } catch (JsonProcessingException ex) {
                            // If parsing fails, use the status text
                            errorMessage = wcre.getStatusText();
                        }

                        return handleError(exchange, status, errorMessage);
                    }

                    return handleError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
                });
    }

    private Mono<ApiResponse<AuthorizationResponseDTO>> validateToken(String token) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8001")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AuthorizationResponseDTO>>() {});
    }

    private Mono<Void> handleError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Object> errorResponse = ApiResponse.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing error response", e);
            return exchange.getResponse().setComplete();
        }
    }
}