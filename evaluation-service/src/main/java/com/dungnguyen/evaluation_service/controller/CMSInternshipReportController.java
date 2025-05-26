package com.dungnguyen.evaluation_service.controller;

import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.exception.InternshipReportNotFoundException;
import com.dungnguyen.evaluation_service.response.ApiResponse;
import com.dungnguyen.evaluation_service.service.CMSInternshipReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cms/internship-reports")
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipReportController {

    private final CMSInternshipReportService cmsReportService;

    /**
     * Get all internship reports
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSInternshipReportDTO>>> getAllReports(
            @RequestParam(required = false) String periodId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Boolean submitted) {
        try {
            List<CMSInternshipReportDTO> reports = cmsReportService.getAllReports(periodId, studentName, companyName, submitted);

            return ResponseEntity.ok(ApiResponse.<List<CMSInternshipReportDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Reports retrieved successfully")
                    .data(reports)
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving reports: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipReportDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving reports")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get report by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipReportDetailDTO>> getReportById(@PathVariable Integer id) {
        try {
            CMSInternshipReportDetailDTO report = cmsReportService.getReportById(id);

            return ResponseEntity.ok(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Report retrieved successfully")
                    .data(report)
                    .build());

        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the report")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new internship report
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CMSInternshipReportDetailDTO>> createReport(
            @RequestPart("data") CMSInternshipReportCreateDTO createDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            CMSInternshipReportDetailDTO createdReport = cmsReportService.createReport(createDTO, file);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Report created successfully")
                            .data(createdReport)
                            .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating the report")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update internship report
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipReportDetailDTO>> updateReport(
            @PathVariable Integer id,
            @RequestBody CMSInternshipReportUpdateDTO updateDTO) {
        try {
            CMSInternshipReportDetailDTO updatedReport = cmsReportService.updateReport(id, updateDTO);

            return ResponseEntity.ok(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Report updated successfully")
                    .data(updatedReport)
                    .build());

        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the report")
                            .data(null)
                            .build());
        }
    }

    /**
     * Upload file for existing report
     */
    @PutMapping("/{id}/upload-file")
    public ResponseEntity<ApiResponse<CMSInternshipReportDetailDTO>> uploadFile(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("File is required")
                                .data(null)
                                .build());
            }

            CMSInternshipReportDetailDTO updatedReport = cmsReportService.uploadFile(id, file);

            return ResponseEntity.ok(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("File uploaded successfully")
                    .data(updatedReport)
                    .build());

        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipReportDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while uploading the file")
                            .data(null)
                            .build());
        }
    }

    /**
     * Delete internship report (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReport(@PathVariable Integer id) {
        try {
            cmsReportService.deleteReport(id);

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Report deleted successfully")
                    .data(null)
                    .build());

        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while deleting the report")
                            .data(null)
                            .build());
        }
    }
}