package com.dungnguyen.evaluation_service.controller;

import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.exception.UnauthorizedAccessException;
import com.dungnguyen.evaluation_service.exception.InternshipEvaluationNotFoundException;
import com.dungnguyen.evaluation_service.response.ApiResponse;
import com.dungnguyen.evaluation_service.service.CompanyEvaluationService;
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
public class CompanyEvaluationController {

    private final CompanyEvaluationService companyEvaluationService;

    /**
     * Get all internship progress for company's students with evaluation info
     */
    @GetMapping("/internships")
    public ResponseEntity<ApiResponse<List<CompanyInternshipEvaluationDTO>>> getCompanyInternships(
            @RequestParam(required = false) String periodId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<CompanyInternshipEvaluationDTO> internships = companyEvaluationService
                    .getCompanyInternships(periodId, authHeader);

            return ResponseEntity.ok(ApiResponse.<List<CompanyInternshipEvaluationDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company internships retrieved successfully")
                    .data(internships)
                    .build());

        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<List<CompanyInternshipEvaluationDTO>>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company internships: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CompanyInternshipEvaluationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving company internships")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get detailed evaluation for a specific student
     */
    @GetMapping("/evaluations/{progressId}")
    public ResponseEntity<ApiResponse<CompanyEvaluationDetailResponseDTO>> getEvaluationDetail(
            @PathVariable Integer progressId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CompanyEvaluationDetailResponseDTO evaluation = companyEvaluationService
                    .getEvaluationDetail(progressId, authHeader);

            return ResponseEntity.ok(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation detail retrieved successfully")
                    .data(evaluation)
                    .build());

        } catch (InternshipEvaluationNotFoundException e) {
            log.error("Evaluation not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving evaluation detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the evaluation detail")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create or update evaluation for a student
     */
    @PutMapping("/evaluations/{progressId}")
    public ResponseEntity<ApiResponse<CompanyEvaluationDetailResponseDTO>> updateEvaluation(
            @PathVariable Integer progressId,
            @RequestBody CompanyEvaluationUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CompanyEvaluationDetailResponseDTO updatedEvaluation = companyEvaluationService
                    .updateEvaluation(progressId, updateDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation updated successfully")
                    .data(updatedEvaluation)
                    .build());

        } catch (InternshipEvaluationNotFoundException e) {
            log.error("Evaluation not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating evaluation: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CompanyEvaluationDetailResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the evaluation")
                            .data(null)
                            .build());
        }
    }
}