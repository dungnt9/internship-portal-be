package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipApplicationDetailNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSInternshipApplicationDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/application-details")
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipApplicationDetailController {

    private final CMSInternshipApplicationDetailService cmsInternshipApplicationDetailService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSInternshipApplicationDetailDTO>>> getAllApplicationDetails() {
        try {
            List<CMSInternshipApplicationDetailDTO> applicationDetails = cmsInternshipApplicationDetailService.getAllApplicationDetails();
            return ResponseEntity.ok(ApiResponse.success(applicationDetails, "Application details retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all application details: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipApplicationDetailDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving application details")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDetailDTO>> getApplicationDetailById(@PathVariable Integer id) {
        try {
            CMSInternshipApplicationDetailDTO applicationDetail = cmsInternshipApplicationDetailService.getApplicationDetailById(id);
            return ResponseEntity.ok(ApiResponse.success(applicationDetail, "Application detail retrieved successfully"));
        } catch (InternshipApplicationDetailNotFoundException e) {
            log.error("Application detail not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving application detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the application detail")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApiResponse<List<CMSInternshipApplicationDetailDTO>>> getApplicationDetailsByApplicationId(
            @PathVariable Integer applicationId) {
        try {
            List<CMSInternshipApplicationDetailDTO> applicationDetails =
                    cmsInternshipApplicationDetailService.getApplicationDetailsByApplicationId(applicationId);
            return ResponseEntity.ok(ApiResponse.success(applicationDetails, "Application details retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving application details by application ID: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipApplicationDetailDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving application details")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDetailDTO>> createApplicationDetail(
            @RequestBody CMSInternshipApplicationDetailCreateDTO createDTO) {
        try {
            CMSInternshipApplicationDetailDTO createdApplicationDetail =
                    cmsInternshipApplicationDetailService.createApplicationDetail(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdApplicationDetail, "Application detail created successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating application detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating application detail: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDetailDTO>> updateApplicationDetail(
            @PathVariable Integer id,
            @RequestBody CMSInternshipApplicationDetailUpdateDTO updateDTO) {
        try {
            CMSInternshipApplicationDetailDTO updatedApplicationDetail =
                    cmsInternshipApplicationDetailService.updateApplicationDetail(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedApplicationDetail, "Application detail updated successfully"));
        } catch (InternshipApplicationDetailNotFoundException e) {
            log.error("Application detail not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating application detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating application detail: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteApplicationDetail(@PathVariable Integer id) {
        try {
            cmsInternshipApplicationDetailService.deleteApplicationDetail(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Application detail deleted successfully")
                    .data(null)
                    .build());
        } catch (InternshipApplicationDetailNotFoundException e) {
            log.error("Application detail not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting application detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error deleting application detail: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}