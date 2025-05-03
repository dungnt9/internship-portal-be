package com.dungnguyen.user_service.client;

import com.dungnguyen.user_service.dto.auth.AuthorizationResponseDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    public UserResponseDTO getUserById(Integer userId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Forward the authorization token
            if (token != null && !token.isEmpty()) {
                headers.set(HttpHeaders.AUTHORIZATION, token);
            }

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<UserResponseDTO>> response = restTemplate.exchange(
                    authServiceUrl + "/api/users/" + userId,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<UserResponseDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to get user from Auth Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling Auth Service: {}", e.getMessage());
            return null;
        }
    }

    public Integer getUserIdFromToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<AuthorizationResponseDTO>> response = restTemplate.exchange(
                    authServiceUrl + "/validate-token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<AuthorizationResponseDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                AuthorizationResponseDTO authData = response.getBody().getData();
                if (authData != null && authData.getUserId() != null) {
                    return Integer.parseInt(authData.getUserId());
                }
            }

            log.error("Failed to validate token. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return null;
        }
    }
}