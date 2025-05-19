package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.cms.CMSAdminCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSAdminDTO;
import com.dungnguyen.user_service.dto.cms.CMSAdminUpdateDTO;
import com.dungnguyen.user_service.exception.AdminNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CMSAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/admins")
@RequiredArgsConstructor
@Slf4j
public class CMSAdminController {

    private final CMSAdminService cmsAdminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSAdminDTO>>> getAllAdmins() {
        try {
            List<CMSAdminDTO> admins = cmsAdminService.getAllAdmins();
            return ResponseEntity.ok(ApiResponse.success(admins, "Admins retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all admins: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSAdminDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving admins")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSAdminDTO>> getAdminById(@PathVariable Integer id) {
        try {
            CMSAdminDTO admin = cmsAdminService.getAdminById(id);
            return ResponseEntity.ok(ApiResponse.success(admin, "Admin retrieved successfully"));
        } catch (AdminNotFoundException e) {
            log.error("Admin not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving admin: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the admin")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSAdminDTO>> createAdmin(@RequestBody CMSAdminCreateDTO createDTO) {
        try {
            CMSAdminDTO createdAdmin = cmsAdminService.createAdmin(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdAdmin, "Admin created successfully"));
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating admin: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating admin: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSAdminDTO>> updateAdmin(
            @PathVariable Integer id,
            @RequestBody CMSAdminUpdateDTO updateDTO) {
        try {
            CMSAdminDTO updatedAdmin = cmsAdminService.updateAdmin(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedAdmin, "Admin updated successfully"));
        } catch (AdminNotFoundException e) {
            log.error("Admin not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating admin: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSAdminDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating admin: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}