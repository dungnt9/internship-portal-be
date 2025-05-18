// StudentProgressController.java
package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipProgressDetailDTO;
import com.dungnguyen.registration_service.dto.InternshipProgressUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.StudentProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-progress")
@RequiredArgsConstructor
@Slf4j
public class StudentProgressController {

    private final StudentProgressService progressService;

    /**
     * Get the current internship progress for the logged-in student
     *
     * @param authHeader Authorization header
     * @return InternshipProgressDetailDTO with progress details
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<InternshipProgressDetailDTO>> getCurrentProgress(
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipProgressDetailDTO progressDetail = progressService.getCurrentProgress(authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipProgressDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Current internship progress retrieved successfully")
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
            log.error("Error retrieving current progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipProgressDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the current progress")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update the current internship progress information
     *
     * @param updateDTO Updated information
     * @param authHeader Authorization header
     * @return Updated InternshipProgressDetailDTO
     */
    @PutMapping("/current")
    public ResponseEntity<ApiResponse<InternshipProgressDetailDTO>> updateProgress(
            @RequestBody InternshipProgressUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipProgressDetailDTO updatedProgress = progressService.updateProgress(updateDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipProgressDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship progress updated successfully")
                    .data(updatedProgress)
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
            log.error("Error updating progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipProgressDetailDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the progress")
                            .data(null)
                            .build());
        }
    }
}