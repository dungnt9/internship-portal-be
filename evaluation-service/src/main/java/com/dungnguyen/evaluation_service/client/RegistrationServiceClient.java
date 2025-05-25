package com.dungnguyen.evaluation_service.client;

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
public class RegistrationServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.registration.url:http://localhost:8003}")
    private String registrationServiceUrl;

    /**
     * Get current active internship progress ID for a student
     */
    public Integer getCurrentProgressIdForStudent(Integer studentId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            // Call registration service to get current progress
            ResponseEntity<Object> response = restTemplate.exchange(
                    registrationServiceUrl + "/student-progress/current",
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

                if (data != null && data.containsKey("progress")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> progress = (Map<String, Object>) data.get("progress");
                    if (progress != null && progress.containsKey("id")) {
                        return (Integer) progress.get("id");
                    }
                }
            }

            log.error("Failed to get current progress for student. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error getting current progress for student {}: {}", studentId, e.getMessage());
            return null;
        }
    }
}