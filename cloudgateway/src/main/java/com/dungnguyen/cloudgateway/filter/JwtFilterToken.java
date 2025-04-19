package com.dungnguyen.cloudgateway.filter;

import com.dungnguyen.cloudgateway.model.dto.AuthorizationResponseDTO;
import com.dungnguyen.cloudgateway.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilterToken implements WebFilter {
    private final WebClient.Builder webClientBuilder;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Bypass token kiểm tra các URL cần bỏ qua
//        if (isBypassToken(exchange)) {
//            return chain.filter(exchange);
//        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorization.substring(7);

        return validateToken(token)
                .flatMap(apiResponse -> {
                    if (apiResponse != null && apiResponse.getData() != null) {
                        // Add a custom header to the request

                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                })
                .onErrorResume(e -> {
                    log.error("Token validation error: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

    private Mono<ApiResponse<AuthorizationResponseDTO>> validateToken(String token) {
        WebClient webClient = webClientBuilder.baseUrl("localhost").build();

        return webClient.post()
                .uri("/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AuthorizationResponseDTO>>() {})
                .onErrorResume(e -> {
                    log.error("Error during token validation: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    private boolean isBypassToken(ServerWebExchange exchange) {
        String requestPath = exchange.getRequest().getURI().getPath();
        String requestMethod = exchange.getRequest().getMethod().name();

        // Các route cần bypass
        if (
                requestPath.startsWith("/api/")
        ) {
            return true;
        }

        Map<String, List<String>> bypassTokens = Map.of(
                String.format("%s/setting", "/api/cms"), List.of("GET")
        );

        return bypassTokens.entrySet().stream()
                .anyMatch(entry -> requestPath.contains(entry.getKey()) && entry.getValue().contains(requestMethod));
    }
}