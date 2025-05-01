package com.dungnguyen.auth_service.controller;

import com.dungnguyen.auth_service.dto.ChangePasswordRequestDTO;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.response.ApiResponse;
import com.dungnguyen.auth_service.service.PasswordChangeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PasswordChangeController {

    private final PasswordChangeService passwordChangeService;

    @Value("${jwt.secret:defaultSecretKeyWithAtLeast256BitsLengthForHS256Algorithm}")
    private String secret;

    /**
     * Extract user ID from JWT token
     */
    private Integer extractUserIdFromToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validate password change request
     */
    private Map<String, String> validatePasswordChangeRequest(ChangePasswordRequestDTO request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isEmpty()) {
            errors.put("currentPassword", "Current password is required");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            errors.put("newPassword", "New password is required");
        } else if (request.getNewPassword().length() < 8) {
            errors.put("newPassword", "New password must be at least 8 characters long");
        }

        if (request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty()) {
            errors.put("confirmPassword", "Password confirmation is required");
        } else if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", "Password confirmation does not match");
        }

        return errors;
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid ChangePasswordRequestDTO request) {

        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            String token = authHeader.substring(7);

            // Extract user ID from token
            Integer userId = extractUserIdFromToken(token);
            if (userId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid authentication token")
                                .data(null)
                                .build());
            }

            // Validate request
            Map<String, String> validationErrors = validatePasswordChangeRequest(request);
            if (!validationErrors.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Validation failed")
                                .data(validationErrors)
                                .build());
            }

            // Change password
            boolean success = passwordChangeService.changePassword(
                    userId,
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );

            if (success) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Password changed successfully")
                        .data(null)
                        .build());
            } else {
                // Get the specific error message from the service
                String errorMessage = passwordChangeService.getLastErrorMessage();
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(errorMessage)
                                .data(null)
                                .build());
            }

        } catch (UserNotFoundException e) {
            log.error("User not found during password change: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while changing password")
                            .data(null)
                            .build());
        }
    }
}
