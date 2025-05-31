package com.dungnguyen.evaluation_service.controller;

import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.exception.InternshipReportNotFoundException;
import com.dungnguyen.evaluation_service.exception.UnauthorizedAccessException;
import com.dungnguyen.evaluation_service.exception.ReportAlreadySubmittedException;
import com.dungnguyen.evaluation_service.response.ApiResponse;
import com.dungnguyen.evaluation_service.service.StudentEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentEvaluationController {

    private final StudentEvaluationService studentEvaluationService;

    /**
     * Get internship report for current student
     */
    @GetMapping("/my-report")
    public ResponseEntity<ApiResponse<InternshipReportDTO>> getMyReport(
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipReportDTO report = studentEvaluationService.getMyReport(authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipReportDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Report retrieved successfully")
                    .data(report)
                    .build());

        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the report")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update internship report for current student
     * UPDATED: Handle ReportAlreadySubmittedException
     */
    @PutMapping(value = "/my-report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<InternshipReportDTO>> updateMyReport(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart(value = "data", required = false) InternshipReportUpdateDTO updateDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Validate that at least one field is provided
            if ((updateDTO == null || (
                    (updateDTO.getTitle() == null || updateDTO.getTitle().trim().isEmpty()) &&
                            (updateDTO.getContent() == null || updateDTO.getContent().trim().isEmpty())
            )) && (file == null || file.isEmpty())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<InternshipReportDTO>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("At least one field (title, content, or file) must be provided")
                                .data(null)
                                .build());
            }

            // Set default DTO if null
            if (updateDTO == null) {
                updateDTO = new InternshipReportUpdateDTO();
            }

            InternshipReportDTO updatedReport = studentEvaluationService.updateMyReport(authHeader, updateDTO, file);

            return ResponseEntity.ok(ApiResponse.<InternshipReportDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Report updated successfully")
                    .data(updatedReport)
                    .build());

        } catch (ReportAlreadySubmittedException e) {
            log.error("Report already submitted: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.CONFLICT.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (InternshipReportNotFoundException e) {
            log.error("Report not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating report: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipReportDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the report")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get company evaluation for current student
     */
    @GetMapping("/my-evaluation")
    public ResponseEntity<ApiResponse<CompanyEvaluationResponseDTO>> getMyEvaluation(
            @RequestHeader("Authorization") String authHeader) {
        try {
            CompanyEvaluationResponseDTO evaluation = studentEvaluationService.getMyEvaluation(authHeader);

            if (evaluation == null) {
                return ResponseEntity.ok(ApiResponse.<CompanyEvaluationResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("No evaluation available yet")
                        .data(null)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.<CompanyEvaluationResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation retrieved successfully")
                    .data(evaluation)
                    .build());

        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyEvaluationResponseDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving evaluation: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyEvaluationResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the evaluation")
                            .data(null)
                            .build());
        }
    }
}