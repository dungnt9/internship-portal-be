package com.dungnguyen.registration_service.client;

import com.dungnguyen.registration_service.dto.CompanyDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.user.url}")
    private String userServiceUrl;

    public CompanyDTO getCompanyById(Integer companyId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Forward the authorization token if provided
            if (token != null && !token.isEmpty()) {
                headers.set(HttpHeaders.AUTHORIZATION, token);
            }

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<CompanyDTO>> response = restTemplate.exchange(
                    userServiceUrl + "/companies/" + companyId,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<CompanyDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to get company from User Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling User Service: {}", e.getMessage());
            return null;
        }
    }

    public List<CompanyDTO> getAllCompanies(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Forward the authorization token if provided
            if (token != null && !token.isEmpty()) {
                headers.set(HttpHeaders.AUTHORIZATION, token);
            }

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<List<CompanyDTO>>> response = restTemplate.exchange(
                    userServiceUrl + "/companies/all",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<List<CompanyDTO>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to get all companies from User Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling User Service: {}", e.getMessage());
            return null;
        }
    }

    public StudentDTO getStudentById(Integer studentId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Forward the authorization token if provided
            if (token != null && !token.isEmpty()) {
                headers.set(HttpHeaders.AUTHORIZATION, token);
            }

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<StudentDTO>> response = restTemplate.exchange(
                    userServiceUrl + "/students/" + studentId,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<StudentDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to get student from User Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling User Service: {}", e.getMessage());
            return null;
        }
    }
}