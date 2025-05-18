package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipProgressConfirmDTO;
import com.dungnguyen.registration_service.dto.InternshipProgressDetailDTO;
import com.dungnguyen.registration_service.dto.InternshipProgressDTO;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.TeacherProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-progress")
@RequiredArgsConstructor
@Slf4j
public class TeacherProgressController {

    private final TeacherProgressService progressService;

    /**
     * Get all internship progress records assigned to the current teacher
     * This endpoint is accessible to teacher role only
     *
     * @param periodId Optional period ID filter
     * @param status Optional status filter (IN_PROGRESS, COMPLETED, CANCELLED)
     * @param authHeader Authorization header
     * @return List of InternshipProgressDetailDTO with student details
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipProgressDetailDTO>>> getTeacherProgress(
            @RequestParam(required = false) String periodId,
            @RequestParam(required = false) String status,
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<InternshipProgressDetailDTO> progressList = progressService.getProgressForTeacher(
                    periodId, status, authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipProgressDetailDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship progress retrieved successfully")
                    .data(progressList)
                    .build());

        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<List<InternshipProgressDetailDTO>>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipProgressDetailDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship progress")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get detailed information about a specific internship progress
     * This endpoint is accessible to teacher role only
     *
     * @param progressId Internship progress ID
     * @param authHeader Authorization header
     * @return InternshipProgressDetailDTO with student and company details
     */
    @GetMapping("/{progressId}")
    public ResponseEntity<ApiResponse<InternshipProgressDetailDTO>> getProgressDetail(
            @PathVariable Integer progressId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipProgressDetailDTO progressDetail = progressService.getProgressDetail(progressId, authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipProgressDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship progress detail retrieved successfully")
                    .data(progressDetail)
                    .build());

        } catch (InternshipProgressNotFoundException e) {
            log.error("Progress not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipProgressDetailDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<InternshipProgressDetailDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving progress detail: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipProgressDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the progress detail")
                            .data(null)
                            .build());
        }
    }

    /**
     * Confirm a student's internship progress
     * This endpoint is accessible to teacher role only
     *
     * @param progressId Internship progress ID
     * @param confirmDTO Confirmation data
     * @param authHeader Authorization header
     * @return Updated InternshipProgressDTO
     */
    @PutMapping("/{progressId}/confirm")
    public ResponseEntity<ApiResponse<InternshipProgressDTO>> confirmProgress(
            @PathVariable Integer progressId,
            @RequestBody(required = false) InternshipProgressConfirmDTO confirmDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipProgressDTO updatedProgress = progressService.confirmProgress(progressId, confirmDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipProgressDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship progress confirmed successfully")
                    .data(updatedProgress)
                    .build());

        } catch (InternshipProgressNotFoundException e) {
            log.error("Progress not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipProgressDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<InternshipProgressDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error confirming progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipProgressDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while confirming the progress")
                            .data(null)
                            .build());
        }
    }
}