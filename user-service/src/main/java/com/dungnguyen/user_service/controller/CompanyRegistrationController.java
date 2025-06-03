package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.CompanyRegistrationDTO;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CompanyRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyRegistrationController {

    private final CompanyRegistrationService companyRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerCompany(@RequestBody CompanyRegistrationDTO registrationDTO) {
        try {
            // Validate required fields
            if (registrationDTO.getCompanyName() == null || registrationDTO.getCompanyName().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Company name is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getWebsite() == null || registrationDTO.getWebsite().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Website is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getAddress() == null || registrationDTO.getAddress().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Address is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getFullName() == null || registrationDTO.getFullName().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Contact name is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getEmail() == null || registrationDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Email is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getPhone() == null || registrationDTO.getPhone().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Phone is required")
                                .data(null)
                                .build());
            }

            if (registrationDTO.getPosition() == null || registrationDTO.getPosition().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Position is required")
                                .data(null)
                                .build());
            }

            // Register the company
            companyRegistrationService.registerCompany(registrationDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<String>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Company registration successful. Password has been sent to your email.")
                            .data("Registration successful")
                            .build());

        } catch (IllegalArgumentException e) {
            log.error("Validation error during company registration: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error during company registration: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<String>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Registration failed. Please try again later.")
                            .data(null)
                            .build());
        }
    }
}