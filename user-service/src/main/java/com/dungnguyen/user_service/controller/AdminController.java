package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.AdminDTO;
import com.dungnguyen.user_service.dto.AdminUpdateDTO;
import com.dungnguyen.user_service.exception.AdminNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminDTO>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AdminDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = adminService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AdminDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Get admin profile
            AdminDTO adminDTO = adminService.getAdminByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<AdminDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Admin profile retrieved successfully")
                    .data(adminDTO)
                    .build());

        } catch (AdminNotFoundException e) {
            log.error("Admin not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<AdminDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving admin profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AdminDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the admin profile")
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<AdminDTO>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AdminUpdateDTO updateDTO) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AdminDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = adminService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<AdminDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Update admin profile
            AdminDTO updatedAdmin = adminService.updateAdmin(authUserId, updateDTO);

            // Add auth information to the response
            AdminDTO fullAdminDTO = adminService.getAdminByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<AdminDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Admin profile updated successfully")
                    .data(fullAdminDTO)
                    .build());

        } catch (AdminNotFoundException e) {
            log.error("Admin not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<AdminDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating admin profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AdminDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the admin profile")
                            .data(null)
                            .build());
        }
    }
}