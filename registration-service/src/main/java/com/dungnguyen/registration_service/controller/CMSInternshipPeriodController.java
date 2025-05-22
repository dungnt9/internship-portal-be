package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSInternshipPeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/periods")
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipPeriodController {

    private final CMSInternshipPeriodService cmsInternshipPeriodService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSInternshipPeriodDTO>>> getAllPeriods() {
        try {
            List<CMSInternshipPeriodDTO> periods = cmsInternshipPeriodService.getAllPeriods();
            return ResponseEntity.ok(ApiResponse.success(periods, "Internship periods retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all internship periods: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSInternshipPeriodDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship periods")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipPeriodDTO>> getPeriodById(@PathVariable String id) {
        try {
            CMSInternshipPeriodDTO period = cmsInternshipPeriodService.getPeriodById(id);
            return ResponseEntity.ok(ApiResponse.success(period, "Internship period retrieved successfully"));
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Internship period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving internship period: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the internship period")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSInternshipPeriodDTO>> createPeriod(@RequestBody CMSInternshipPeriodCreateDTO createDTO) {
        try {
            CMSInternshipPeriodDTO createdPeriod = cmsInternshipPeriodService.createPeriod(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdPeriod, "Internship period created successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating internship period: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating internship period: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSInternshipPeriodDTO>> updatePeriod(
            @PathVariable String id,
            @RequestBody CMSInternshipPeriodUpdateDTO updateDTO) {
        try {
            CMSInternshipPeriodDTO updatedPeriod = cmsInternshipPeriodService.updatePeriod(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedPeriod, "Internship period updated successfully"));
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Internship period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating internship period: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSInternshipPeriodDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating internship period: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePeriod(@PathVariable String id) {
        try {
            cmsInternshipPeriodService.deletePeriod(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship period deleted successfully")
                    .data(null)
                    .build());
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Internship period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting internship period: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error deleting internship period: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}