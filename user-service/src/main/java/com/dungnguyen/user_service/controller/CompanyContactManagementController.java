package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.*;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CompanyContactManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/contacts")
@RequiredArgsConstructor
@Slf4j
public class CompanyContactManagementController {

    private final CompanyContactManagementService companyContactManagementService;

    /**
     * GET /companies/contacts - Get all company contacts of my company
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyContactDetailDTO>>> getMyCompanyContacts(
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<CompanyContactDetailDTO>>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            Integer authUserId = companyContactManagementService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<CompanyContactDetailDTO>>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            List<CompanyContactDetailDTO> contacts = companyContactManagementService.getMyCompanyContacts(authUserId);

            return ResponseEntity.ok(ApiResponse.<List<CompanyContactDetailDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contacts retrieved successfully")
                    .data(contacts)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<List<CompanyContactDetailDTO>>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company contacts: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CompanyContactDetailDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving company contacts")
                            .data(null)
                            .build());
        }
    }

    /**
     * GET /companies/contacts/{id} - Get specific company contact by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyContactDetailDTO>> getCompanyContactById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            Integer authUserId = companyContactManagementService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            CompanyContactDetailDTO contact = companyContactManagementService.getCompanyContactById(authUserId, id);

            return ResponseEntity.ok(ApiResponse.<CompanyContactDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contact retrieved successfully")
                    .data(contact)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Company contact not found")
                            .data(null)
                            .build());
        }
    }

    /**
     * POST /companies/contacts - Create new company contact
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyContactDetailDTO>> createCompanyContact(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCompanyContactDTO createDTO) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            Integer authUserId = companyContactManagementService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            CompanyContactDetailDTO createdContact = companyContactManagementService.createCompanyContact(authUserId, createDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Company contact created successfully")
                            .data(createdContact)
                            .build());

        } catch (IllegalArgumentException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (RuntimeException e) {
            log.error("Error creating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error creating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating company contact")
                            .data(null)
                            .build());
        }
    }

    /**
     * PUT /companies/contacts/{id} - Update company contact
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyContactDetailDTO>> updateCompanyContact(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @RequestBody UpdateCompanyContactDTO updateDTO) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            Integer authUserId = companyContactManagementService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDetailDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            CompanyContactDetailDTO updatedContact = companyContactManagementService.updateCompanyContact(authUserId, id, updateDTO);

            return ResponseEntity.ok(ApiResponse.<CompanyContactDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contact updated successfully")
                    .data(updatedContact)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (RuntimeException e) {
            log.error("Error updating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error updating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyContactDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating company contact")
                            .data(null)
                            .build());
        }
    }

    /**
     * DELETE /companies/contacts/{id} - Delete company contact
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompanyContact(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Void>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            Integer authUserId = companyContactManagementService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Void>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            companyContactManagementService.deleteCompanyContact(authUserId, id);

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contact deleted successfully")
                    .data(null)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (RuntimeException e) {
            log.error("Error deleting company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error deleting company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while deleting company contact")
                            .data(null)
                            .build());
        }
    }
}