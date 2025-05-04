package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.CompanyDTO;
import com.dungnguyen.user_service.dto.CompanyUpdateDTO;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompanyById(@PathVariable Integer id) {
        try {
            CompanyDTO companyDTO = companyService.getCompanyById(id);

            return ResponseEntity.ok(ApiResponse.<CompanyDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company retrieved successfully")
                    .data(companyDTO)
                    .build());

        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the company")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getAllCompanies() {
        try {
            List<CompanyDTO> companies = companyService.getAllCompanies();

            return ResponseEntity.ok(ApiResponse.<List<CompanyDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Companies retrieved successfully")
                    .data(companies)
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving companies: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CompanyDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving companies")
                            .data(null)
                            .build());
        }
    }

    // only admin update any company
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(
            @PathVariable Integer id,
            @RequestBody CompanyUpdateDTO updateDTO) {
        try {
            CompanyDTO updatedCompany = companyService.updateCompany(id, updateDTO);

            return ResponseEntity.ok(ApiResponse.<CompanyDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company updated successfully")
                    .data(updatedCompany)
                    .build());

        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the company")
                            .data(null)
                            .build());
        }
    }

    // For company contacts to get their own company
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<CompanyDTO>> getMyCompany(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = companyService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            CompanyDTO companyDTO = companyService.getMyCompany(authUserId);

            return ResponseEntity.ok(ApiResponse.<CompanyDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("My company retrieved successfully")
                    .data(companyDTO)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving my company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving your company")
                            .data(null)
                            .build());
        }
    }

    // For company contacts to update their own company
    @PutMapping("/my")
    public ResponseEntity<ApiResponse<CompanyDTO>> updateMyCompany(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CompanyUpdateDTO updateDTO) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = companyService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            CompanyDTO updatedCompany = companyService.updateMyCompany(authUserId, updateDTO);

            return ResponseEntity.ok(ApiResponse.<CompanyDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("My company updated successfully")
                    .data(updatedCompany)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating my company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating your company")
                            .data(null)
                            .build());
        }
    }
}