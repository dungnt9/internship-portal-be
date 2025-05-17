package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.ApplicationActionDTO;
import com.dungnguyen.registration_service.dto.StudentApplicationDTO;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CompanyApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-applications")
@RequiredArgsConstructor
@Slf4j
public class CompanyApplicationController {

    private final CompanyApplicationService applicationService;

    /**
     * Get pending applications for the current company
     * This endpoint is accessible to company contact role only
     *
     * @param authHeader Authorization header
     * @return List of StudentApplicationDTO with student details
     */
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<StudentApplicationDTO>>> getPendingApplications(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<StudentApplicationDTO> applications = applicationService.getPendingApplicationsForCompany(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<StudentApplicationDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Pending applications retrieved successfully")
                    .data(applications)
                    .build());

        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<List<StudentApplicationDTO>>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving pending applications: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<StudentApplicationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving applications")
                            .data(null)
                            .build());
        }
    }

    /**
     * Take action on an application (approve or reject)
     * This endpoint is accessible to company contact role only
     *
     * @param actionDTO Application action data
     * @param authHeader Authorization header
     * @return Updated StudentApplicationDTO
     */
    @PutMapping("/action")
    public ResponseEntity<ApiResponse<StudentApplicationDTO>> takeActionOnApplication(
            @RequestBody ApplicationActionDTO actionDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            StudentApplicationDTO result = applicationService.processApplicationAction(actionDTO, authHeader);

            String message = "APPROVE".equals(actionDTO.getAction())
                    ? "Application approved successfully"
                    : "Application rejected successfully";

            return ResponseEntity.ok(ApiResponse.<StudentApplicationDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message(message)
                    .data(result)
                    .build());

        } catch (InternshipApplicationNotFoundException e) {
            log.error("Application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<StudentApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<StudentApplicationDTO>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error processing application action: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<StudentApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while processing the application")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get all applications history for the current company (optional: filter by status)
     * This endpoint is accessible to company contact role only
     *
     * @param status Optional status filter
     * @param authHeader Authorization header
     * @return List of StudentApplicationDTO
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<StudentApplicationDTO>>> getApplicationsHistory(
            @RequestParam(required = false) String periodId,
            @RequestParam(required = false) String status,
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<StudentApplicationDTO> applications = applicationService.getApplicationsHistoryForCompany(
                    periodId, status, authHeader);

            return ResponseEntity.ok(ApiResponse.<List<StudentApplicationDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Applications history retrieved successfully")
                    .data(applications)
                    .build());

        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<List<StudentApplicationDTO>>builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving applications history: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<StudentApplicationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving applications history")
                            .data(null)
                            .build());
        }
    }
}