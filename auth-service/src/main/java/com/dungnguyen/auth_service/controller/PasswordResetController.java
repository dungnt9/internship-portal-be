package com.dungnguyen.auth_service.controller;

import com.dungnguyen.auth_service.dto.ForgotPasswordRequestDTO;
import com.dungnguyen.auth_service.dto.ResetPasswordRequestDTO;
import com.dungnguyen.auth_service.dto.VerifyOtpRequestDTO;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.response.ApiResponse;
import com.dungnguyen.auth_service.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * Step 1: Request password reset by providing email
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO request) {
        try {
            if (request == null || request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Email is required")
                                .data(null)
                                .build());
            }

            passwordResetService.processForgotPassword(request.getEmail());

            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("OTP has been sent to your email")
                    .data(null)
                    .build());

        } catch (UserNotFoundException e) {
            log.warn("Forgot password attempt for non-existent email: {}", request.getEmail());
            // Return success message even if user doesn't exist to prevent email enumeration
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("If your email is registered, you will receive an OTP")
                    .data(null)
                    .build());
        } catch (Exception e) {
            log.error("Error processing forgot password request: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while processing your request")
                            .data(null)
                            .build());
        }
    }

    /**
     * Step 2: Verify the OTP sent to the user's email
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody @Valid VerifyOtpRequestDTO request) {
        try {
            if (request == null || request.getEmail() == null || request.getOtp() == null ||
                    request.getEmail().trim().isEmpty() || request.getOtp().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<Boolean>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Email and OTP are required")
                                .data(false)
                                .build());
            }

            boolean isValid = passwordResetService.verifyOtp(request.getEmail(), request.getOtp());

            HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            String message = isValid ? "OTP verification successful" : "Invalid or expired OTP";

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.<Boolean>builder()
                            .status(status.value())
                            .message(message)
                            .data(isValid)
                            .build());

        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while verifying OTP")
                            .data(false)
                            .build());
        }
    }

    /**
     * Step 3: Reset password with the verified OTP and new password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO request) {
        try {
            if (request == null || request.getEmail() == null || request.getOtp() == null || request.getNewPassword() == null ||
                    request.getEmail().trim().isEmpty() || request.getOtp().trim().isEmpty() || request.getNewPassword().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<Boolean>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Email, OTP, and new password are required")
                                .data(false)
                                .build());
            }

            // Check password strength
            if (request.getNewPassword().length() < 8) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<Boolean>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Password must be at least 8 characters long")
                                .data(false)
                                .build());
            }

            boolean isSuccess = passwordResetService.resetPassword(
                    request.getEmail(), request.getOtp(), request.getNewPassword());

            HttpStatus status = isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            String message = isSuccess ? "Password reset successful" : "Failed to reset password. Invalid or expired OTP.";

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.<Boolean>builder()
                            .status(status.value())
                            .message(message)
                            .data(isSuccess)
                            .build());

        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while resetting password")
                            .data(false)
                            .build());
        }
    }
}