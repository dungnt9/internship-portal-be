package com.dungnguyen.registration_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Value("${services.user.url}")
    private String userServiceUrl;

    public Integer getUserIdFromToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    authServiceUrl + "/validate-token",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Extract user ID from response
                // Implementation depends on your API response structure
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

                if (data != null && data.containsKey("userId")) {
                    return Integer.parseInt(data.get("userId").toString());
                }
            }

            log.error("Failed to validate token. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return null;
        }
    }

    public Integer getUserCompanyId(String token) {
        try {
            Integer userId = getUserIdFromToken(token);
            if (userId == null) {
                return null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            // Call the user service to get the company contact by auth user ID
            ResponseEntity<Object> response = restTemplate.exchange(
                    userServiceUrl + "/companies/me",
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Extract company ID from response
                // Implementation depends on your API response structure
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

                if (data != null && data.containsKey("companyId")) {
                    return (Integer) data.get("companyId");
                }
            }

            log.error("Failed to get company ID. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error getting company ID: {}", e.getMessage());
            return null;
        }
    }
}