package com.dungnguyen.auth_service.controller;

import com.dungnguyen.auth_service.dto.AuthRequestDTO;
import com.dungnguyen.auth_service.dto.AuthorizationResponseDTO;
import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.response.ApiResponse;
import com.dungnguyen.auth_service.service.AuthService;
import com.dungnguyen.auth_service.utils.JwtUtil;
import com.dungnguyen.auth_service.utils.RequestValidator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;    // token bị biến dạng
import io.jsonwebtoken.SignatureException;    // chữ ký của JWT token không hợp lệ
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;  // đối tượng phản hồi HTTP (response) trong Spring
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // Spring Security sẽ ném thông tin đăng nhập không hợp lệ
import org.springframework.security.authentication.DisabledException;  //Spring Security sẽ ném ngoại lệ nếu tài khoản người dùng bị vô hiệu hóa
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // đối tượng chứa thông tin đăng nhập của người dùng
import org.springframework.security.core.Authentication;  //  interface đại diện cho thông tin xác thực của người dùng đã đăng nhập
import org.springframework.security.core.AuthenticationException; // Spring Security sẽ ném ngoại lệ nếu có lỗi xảy ra trong quá trình xác thực
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RequestValidator requestValidator;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) AuthRequestDTO authRequest) {
        if (authRequest == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Request body is missing")
                            .data(null)
                            .build());
        }

        Map<String, String> validationErrors = requestValidator.validateAuthRequest(authRequest);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Validation failed")
                            .data(validationErrors)
                            .build());
        }

        try {
            log.info("Login attempt with identifier: {}", authRequest.getIdentifier());

            Optional<User> userOptional = authService.findByIdentifier(authRequest.getIdentifier());

            if (userOptional.isEmpty()) {
                log.warn("User not found with identifier: {}", authRequest.getIdentifier());
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AuthorizationResponseDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("User not found. Please check your login credentials.")
                                .data(null)
                                .build());
            }

            User user = userOptional.get();
            log.info("User found with email: {}", user.getEmail());

            // Authenticate user
            try {
                // This is where Spring Security checks the password
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getId().toString(), // Using user ID instead of username
                                authRequest.getPassword()
                        )
                );

                log.info("Authentication successful for user ID: {}", user.getId());

                // Generate JWT token with user ID and role
                String token = jwtUtil.generateToken(
                        user.getId(),
                        user.getRole().getName()
                );

                // Create response
                AuthorizationResponseDTO authResponse = new AuthorizationResponseDTO(
                        token,
                        user.getId().toString(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole().getName(), // Include role in the response
                        new Date() // Current date as creation timestamp
                );

                return ResponseEntity.ok(ApiResponse.success(authResponse, "Authentication successful"));
            } catch (BadCredentialsException e) {
                log.error("Bad credentials for user {}: {}", user.getEmail(), e.getMessage());
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AuthorizationResponseDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid password. Please check your credentials.")
                                .data(null)
                                .build());
            }
        } catch (DisabledException e) {
            log.error("User account is disabled: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Your account is disabled. Please contact support.")
                            .data(null)
                            .build());
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Authentication failed: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An unexpected error occurred. Please try again later.")
                            .data(null)
                            .build());
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<AuthorizationResponseDTO>> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Check for Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Authorization header is missing or invalid")
                            .data(null)
                            .build());
        }

        // Extract token from Authorization header
        String token = authHeader.substring(7);

        try {
            // Extract user ID from token
            String userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            Date createdAt = jwtUtil.extractCreationDate(token);

            // Check if token is valid
            User user = authService.findById(Integer.parseInt(userId));
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AuthorizationResponseDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("User in token not found in system")
                                .data(null)
                                .build());
            }

            if (!user.getIsActive()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AuthorizationResponseDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("User account is disabled")
                                .data(null)
                                .build());
            }

            if (jwtUtil.validateToken(token, userId)) {
                AuthorizationResponseDTO authResponse = new AuthorizationResponseDTO(
                        token,
                        userId,
                        user.getEmail(),
                        user.getPhone(),
                        role, // Include role in the response
                        createdAt
                );

                return ResponseEntity.ok(ApiResponse.success(authResponse, "Token is valid"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AuthorizationResponseDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Token validation failed")
                                .data(null)
                                .build());
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("JWT token has expired")
                            .data(null)
                            .build());
        } catch (MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Invalid JWT token")
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AuthorizationResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred during token validation: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}