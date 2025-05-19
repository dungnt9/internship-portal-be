package com.dungnguyen.user_service.client;

import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.response.ApiResponse;
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
public class CMSAuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    public UserResponseDTO createUser(CreateUserRequestDTO requestDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CreateUserRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

            ResponseEntity<ApiResponse<UserResponseDTO>> response = restTemplate.exchange(
                    authServiceUrl + "/cms/users",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<UserResponseDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to create user in Auth Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling Auth Service: {}", e.getMessage());
            throw e;
        }
    }

    public UserResponseDTO updateUser(Integer userId, CreateUserRequestDTO requestDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CreateUserRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

            ResponseEntity<ApiResponse<UserResponseDTO>> response = restTemplate.exchange(
                    authServiceUrl + "/cms/users/" + userId,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<UserResponseDTO>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to update user in Auth Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling Auth Service: {}", e.getMessage());
            throw e;
        }
    }

    public UserResponseDTO getUserById(Integer userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<UserResponseDTO>> response = restTemplate.exchange(
                    authServiceUrl + "/cms/users/" + userId,
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
            throw e;
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<List<UserResponseDTO>>> response = restTemplate.exchange(
                    authServiceUrl + "/cms/users",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<List<UserResponseDTO>>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            }

            log.error("Failed to get all users from Auth Service. Status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Error calling Auth Service: {}", e.getMessage());
            throw e;
        }
    }
}