package com.dungnguyen.auth_service.utils;

import com.dungnguyen.auth_service.dto.AuthRequestDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestValidator {

    /**
     * @return A map of field errors (empty if no errors)
     */
    public Map<String, String> validateAuthRequest(AuthRequestDTO request) {
        Map<String, String> errors = new HashMap<>();
        if (request == null) {
            errors.put("request", "Request body is required");
            return errors;
        }
        if (request.getIdentifier() == null || request.getIdentifier().trim().isEmpty()) {
            errors.put("identifier", "Identifier (email or phone) is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            errors.put("password", "Password is required");
        }
        return errors;
    }
}