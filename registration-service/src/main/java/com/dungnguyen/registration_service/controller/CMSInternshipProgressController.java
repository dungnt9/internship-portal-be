package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSInternshipProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/progress")
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipProgressController {

    private final CMSInternshipProgressService cmsProgressService;

    /**
     * Get all internship progress records
     *
     * @param authHeader Authorization header
     * @return List of CMSInternshipProgressDTO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSInternshipProgressDTO>>> getAllProgress(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<CMSInternshipProgressDTO> progressList = cmsProgressService.getAllProgress(authHeader);
            return ResponseEntity.ok(ApiResponse.success(progressList, "Internship progress records retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipProgressDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship progress records")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get progress by ID
     *
     * @param id Progress ID
     * @param authHeader Authorization header
     * @return CMSInternshipProgressDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipProgressDTO>> getProgressById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSInternshipProgressDTO progress = cmsProgressService.getProgressById(id, authHeader);
            return ResponseEntity.ok(ApiResponse.success(progress, "Internship progress retrieved successfully"));
        } catch (InternshipProgressNotFoundException e) {
            log.error("Internship progress not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the internship progress")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new internship progress
     *
     * @param createDTO Progress creation data
     * @param authHeader Authorization header
     * @return Created CMSInternshipProgressDTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CMSInternshipProgressDTO>> createProgress(
            @RequestBody CMSInternshipProgressCreateDTO createDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSInternshipProgressDTO createdProgress = cmsProgressService.createProgress(createDTO, authHeader);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdProgress, "Internship progress created successfully"));
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating internship progress: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Update internship progress
     *
     * @param id Progress ID
     * @param updateDTO Update data
     * @param authHeader Authorization header
     * @return Updated CMSInternshipProgressDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipProgressDTO>> updateProgress(
            @PathVariable Integer id,
            @RequestBody CMSInternshipProgressUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSInternshipProgressDTO updatedProgress = cmsProgressService.updateProgress(id, updateDTO, authHeader);
            return ResponseEntity.ok(ApiResponse.success(updatedProgress, "Internship progress updated successfully"));
        } catch (InternshipProgressNotFoundException e) {
            log.error("Internship progress not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipProgressDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating internship progress: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Delete internship progress (soft delete)
     *
     * @param id Progress ID
     * @param authHeader Authorization header
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProgress(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            cmsProgressService.deleteProgress(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship progress deleted successfully")
                    .data(null)
                    .build());
        } catch (InternshipProgressNotFoundException e) {
            log.error("Internship progress not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting internship progress: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error deleting internship progress: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Get progress by period ID
     *
     * @param periodId Period ID
     * @param authHeader Authorization header
     * @return List of CMSInternshipProgressDTO for the specified period
     */
    @GetMapping("/period/{periodId}")
    public ResponseEntity<ApiResponse<List<CMSInternshipProgressDTO>>> getProgressByPeriod(
            @PathVariable String periodId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<CMSInternshipProgressDTO> progressList = cmsProgressService.getProgressByPeriod(periodId, authHeader);
            return ResponseEntity.ok(ApiResponse.success(progressList, "Internship progress for period retrieved successfully"));
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<List<CMSInternshipProgressDTO>>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving progress for period {}: {}", periodId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipProgressDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship progress for period")
                            .data(null)
                            .build());
        }
    }
}