package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSInternshipApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/applications")
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipApplicationController {

    private final CMSInternshipApplicationService cmsInternshipApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSInternshipApplicationDTO>>> getAllApplications() {
        try {
            List<CMSInternshipApplicationDTO> applications = cmsInternshipApplicationService.getAllApplications();
            return ResponseEntity.ok(ApiResponse.success(applications, "Internship applications retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all internship applications: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipApplicationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship applications")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> getApplicationById(@PathVariable Integer id) {
        try {
            CMSInternshipApplicationDTO application = cmsInternshipApplicationService.getApplicationById(id);
            return ResponseEntity.ok(ApiResponse.success(application, "Internship application retrieved successfully"));
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Internship application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving internship application: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the internship application")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> createApplication(@RequestBody CMSInternshipApplicationCreateDTO createDTO) {
        try {
            CMSInternshipApplicationDTO createdApplication = cmsInternshipApplicationService.createApplication(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdApplication, "Internship application created successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating internship application: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating internship application: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PostMapping(value = "/create-with-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> createApplicationWithCV(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("periodId") String periodId,
            @RequestParam("cvFile") MultipartFile cvFile) {
        try {
            // Create DTO from request parameters
            CMSInternshipApplicationCreateDTO createDTO = new CMSInternshipApplicationCreateDTO();
            createDTO.setStudentId(studentId);
            createDTO.setPeriodId(periodId);
            createDTO.setCvFilePath(""); // Will be set during upload

            CMSInternshipApplicationDTO createdApplication = cmsInternshipApplicationService.createApplicationWithCV(createDTO, cvFile);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdApplication, "Internship application created successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating internship application: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating internship application: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> updateApplication(
            @PathVariable Integer id,
            @RequestBody CMSInternshipApplicationUpdateDTO updateDTO) {
        try {
            CMSInternshipApplicationDTO updatedApplication = cmsInternshipApplicationService.updateApplication(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedApplication, "Internship application updated successfully"));
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Internship application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating internship application: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating internship application: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable Integer id) {
        try {
            cmsInternshipApplicationService.deleteApplication(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship application deleted successfully")
                    .data(null)
                    .build());
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Internship application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting internship application: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error deleting internship application: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PostMapping(value = "/{id}/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> uploadCV(
            @PathVariable Integer id,
            @RequestParam("cvFile") MultipartFile cvFile) {
        try {
            CMSInternshipApplicationDTO updatedApplication = cmsInternshipApplicationService.uploadCV(id, cvFile);
            return ResponseEntity.ok(ApiResponse.success(updatedApplication, "CV uploaded successfully"));
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Internship application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error uploading CV: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error uploading CV: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping(value = "/{id}/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CMSInternshipApplicationDTO>> updateCV(
            @PathVariable Integer id,
            @RequestParam("cvFile") MultipartFile cvFile) {
        try {
            CMSInternshipApplicationDTO updatedApplication = cmsInternshipApplicationService.updateCV(id, cvFile);
            return ResponseEntity.ok(ApiResponse.success(updatedApplication, "CV updated successfully"));
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Internship application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating CV: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error updating CV: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}