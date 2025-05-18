package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipProgressDetailDTO;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CompanyProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-progress")
@RequiredArgsConstructor
@Slf4j
public class CompanyProgressController {

    private final CompanyProgressService progressService;

    /**
     * Get internship progress for the current company with optional period filtering
     * This endpoint is accessible to company contact role only
     *
     * @param periodId Optional period ID filter
     * @param authHeader Authorization header
     * @return List of InternshipProgressDetailDTO with student details
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipProgressDetailDTO>>> getCompanyProgress(
            @RequestParam(required = false) String periodId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<InternshipProgressDetailDTO> progressList = progressService.getCompanyProgress(periodId, authHeader);

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
}