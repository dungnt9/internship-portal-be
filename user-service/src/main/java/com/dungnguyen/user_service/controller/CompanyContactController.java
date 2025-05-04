package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.CompanyContactDTO;
import com.dungnguyen.user_service.dto.CompanyContactUpdateDTO;
import com.dungnguyen.user_service.exception.CompanyContactNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CompanyContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyContactController {

    private final CompanyContactService companyContactService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CompanyContactDTO>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = companyContactService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Get company contact profile
            CompanyContactDTO companyContactDTO = companyContactService.getCompanyContactByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<CompanyContactDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contact profile retrieved successfully")
                    .data(companyContactDTO)
                    .build());

        } catch (CompanyContactNotFoundException e) {
            log.error("Company contact not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyContactDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company contact profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyContactDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the company contact profile")
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<CompanyContactDTO>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CompanyContactUpdateDTO updateDTO) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = companyContactService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CompanyContactDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Update company contact profile
            CompanyContactDTO updatedCompanyContact = companyContactService.updateCompanyContact(authUserId, updateDTO);

            // Add auth information to the response
            CompanyContactDTO fullCompanyContactDTO = companyContactService.getCompanyContactByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<CompanyContactDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company contact profile updated successfully")
                    .data(fullCompanyContactDTO)
                    .build());

        } catch (CompanyContactNotFoundException e) {
            log.error("Company contact not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyContactDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating company contact profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyContactDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the company contact profile")
                            .data(null)
                            .build());
        }
    }
}