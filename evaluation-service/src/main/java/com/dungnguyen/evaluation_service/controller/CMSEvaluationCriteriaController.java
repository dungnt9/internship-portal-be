package com.dungnguyen.evaluation_service.controller;

import com.dungnguyen.evaluation_service.dto.EvaluationCriteriaDTO;
import com.dungnguyen.evaluation_service.dto.CMSEvaluationCriteriaCreateDTO;
import com.dungnguyen.evaluation_service.dto.CMSEvaluationCriteriaUpdateDTO;
import com.dungnguyen.evaluation_service.exception.EvaluationCriteriaNotFoundException;
import com.dungnguyen.evaluation_service.response.ApiResponse;
import com.dungnguyen.evaluation_service.service.CMSEvaluationCriteriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/evaluation-criteria")
@RequiredArgsConstructor
@Slf4j
public class CMSEvaluationCriteriaController {

    private final CMSEvaluationCriteriaService criteriaService;

    /**
     * Get all evaluation criteria
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EvaluationCriteriaDTO>>> getAllCriteria() {
        try {
            List<EvaluationCriteriaDTO> criteria = criteriaService.getAllCriteria();

            return ResponseEntity.ok(ApiResponse.<List<EvaluationCriteriaDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation criteria retrieved successfully")
                    .data(criteria)
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<EvaluationCriteriaDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving evaluation criteria")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get evaluation criteria by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluationCriteriaDTO>> getCriteriaById(@PathVariable Integer id) {
        try {
            EvaluationCriteriaDTO criteria = criteriaService.getCriteriaById(id);

            return ResponseEntity.ok(ApiResponse.<EvaluationCriteriaDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation criteria retrieved successfully")
                    .data(criteria)
                    .build());

        } catch (EvaluationCriteriaNotFoundException e) {
            log.error("Evaluation criteria not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the evaluation criteria")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new evaluation criteria
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EvaluationCriteriaDTO>> createCriteria(
            @RequestBody CMSEvaluationCriteriaCreateDTO createDTO) {
        try {
            // Validate input
            if (createDTO.getName() == null || createDTO.getName().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Criteria name is required")
                                .data(null)
                                .build());
            }

            EvaluationCriteriaDTO createdCriteria = criteriaService.createCriteria(createDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Evaluation criteria created successfully")
                            .data(createdCriteria)
                            .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid input for creating evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating the evaluation criteria")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update evaluation criteria
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluationCriteriaDTO>> updateCriteria(
            @PathVariable Integer id,
            @RequestBody CMSEvaluationCriteriaUpdateDTO updateDTO) {
        try {
            EvaluationCriteriaDTO updatedCriteria = criteriaService.updateCriteria(id, updateDTO);

            return ResponseEntity.ok(ApiResponse.<EvaluationCriteriaDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation criteria updated successfully")
                    .data(updatedCriteria)
                    .build());

        } catch (EvaluationCriteriaNotFoundException e) {
            log.error("Evaluation criteria not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid input for updating evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<EvaluationCriteriaDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the evaluation criteria")
                            .data(null)
                            .build());
        }
    }

    /**
     * Delete evaluation criteria (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCriteria(@PathVariable Integer id) {
        try {
            criteriaService.deleteCriteria(id);

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Evaluation criteria deleted successfully")
                    .data(null)
                    .build());

        } catch (EvaluationCriteriaNotFoundException e) {
            log.error("Evaluation criteria not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalStateException e) {
            log.error("Cannot delete evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.CONFLICT.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting evaluation criteria: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while deleting the evaluation criteria")
                            .data(null)
                            .build());
        }
    }
}