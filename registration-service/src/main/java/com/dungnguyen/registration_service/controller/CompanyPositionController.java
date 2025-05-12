package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipPositionCreateDTO;
import com.dungnguyen.registration_service.dto.InternshipPositionDTO;
import com.dungnguyen.registration_service.dto.InternshipPositionUpdateDTO;
import com.dungnguyen.registration_service.exception.InternshipPositionNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.InternshipPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-positions")
@RequiredArgsConstructor
@Slf4j
public class CompanyPositionController {

    private final InternshipPositionService positionService;

    /**
     * Get positions for the current company (company of the authenticated user)
     * This endpoint is accessible to company contact role only
     *
     * @param periodId   Optional period ID filter
     * @param authHeader Authorization header
     * @return List of InternshipPositionDTO with company details
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipPositionDTO>>> getMyCompanyPositions(
            @RequestParam(required = false) String periodId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Get current company ID from token
            Integer companyId = positionService.getCurrentUserCompanyId(authHeader);
            if (companyId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Could not determine company from authorization token")
                                .data(null)
                                .build());
            }

            List<InternshipPositionDTO> positions;
            if (periodId != null) {
                log.info("Getting positions for company ID: {} in period: {}", companyId, periodId);
                positions = positionService.getPositionsByCompanyAndPeriod(companyId, periodId, authHeader);
            } else {
                log.info("Getting all positions for company ID: {} (excluding positions from periods with END status)", companyId);
                positions = positionService.getPositionsByCompany(companyId, authHeader);
            }

            return ResponseEntity.ok(ApiResponse.<List<InternshipPositionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Company positions retrieved successfully")
                    .data(positions)
                    .build());

        } catch (Exception e) {
            log.error("Error getting company positions: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving company positions")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create a new position for the current company
     * This endpoint is accessible to company contact role only
     *
     * @param createDTO  Position creation data
     * @param authHeader Authorization header
     * @return Created InternshipPositionDTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InternshipPositionDTO>> createPosition(
            @RequestBody InternshipPositionCreateDTO createDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Get current company ID from token
            Integer companyId = positionService.getCurrentUserCompanyId(authHeader);
            if (companyId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<InternshipPositionDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Could not determine company from authorization token")
                                .data(null)
                                .build());
            }

            log.info("Creating new position for company ID: {}", companyId);
            InternshipPositionDTO createdPosition = positionService.createPosition(companyId, createDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<InternshipPositionDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Position created successfully")
                            .data(createdPosition)
                            .build());

        } catch (Exception e) {
            log.error("Error creating position: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipPositionDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating the position")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update an existing position for the current company
     * This endpoint is accessible to company contact role only
     *
     * @param positionId Position ID
     * @param updateDTO  Position update data
     * @param authHeader Authorization header
     * @return Updated InternshipPositionDTO
     */
    @PutMapping("/{positionId}")
    public ResponseEntity<ApiResponse<InternshipPositionDTO>> updatePosition(
            @PathVariable Integer positionId,
            @RequestBody InternshipPositionUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Get current company ID from token
            Integer companyId = positionService.getCurrentUserCompanyId(authHeader);
            if (companyId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<InternshipPositionDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Could not determine company from authorization token")
                                .data(null)
                                .build());
            }

            log.info("Updating position ID: {} for company ID: {}", positionId, companyId);
            InternshipPositionDTO updatedPosition = positionService.updatePosition(companyId, positionId, updateDTO);

            return ResponseEntity.ok(ApiResponse.<InternshipPositionDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Position updated successfully")
                    .data(updatedPosition)
                    .build());

        } catch (InternshipPositionNotFoundException e) {
            log.error("Position not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipPositionDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating position: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipPositionDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the position")
                            .data(null)
                            .build());
        }
    }
}