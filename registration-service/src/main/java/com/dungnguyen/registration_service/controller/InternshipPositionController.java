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
@RequestMapping("/positions")
@RequiredArgsConstructor
@Slf4j
public class InternshipPositionController {

    private final InternshipPositionService positionService;

    /**
     * Get all positions with company details
     * This endpoint is accessible to admin role only
     *
     * @param authHeader Authorization header
     * @return List of InternshipPositionDTO with company details
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InternshipPositionDTO>>> getAllPositions(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            log.info("Getting all positions with company details");
            List<InternshipPositionDTO> positions = positionService.getAllPositionsWithCompanyDetails(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipPositionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Positions retrieved successfully")
                    .data(positions)
                    .build());

        } catch (Exception e) {
            log.error("Error getting all positions: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving positions")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get positions by company ID, excluding positions from periods with END status
     * This endpoint is accessible to all authenticated users (all roles)
     *
     * @param companyId Company ID
     * @param authHeader Authorization header
     * @return List of InternshipPositionDTO with company details
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<InternshipPositionDTO>>> getPositionsByCompany(
            @PathVariable Integer companyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            log.info("Getting OPEN positions for company ID: {} in the active period", companyId);
            List<InternshipPositionDTO> positions = positionService.getOpenPositionsByCompanyForActivePeriod(companyId, authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipPositionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Open positions retrieved successfully")
                    .data(positions)
                    .build());

        } catch (Exception e) {
            log.error("Error getting open positions by company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving positions")
                            .data(null)
                            .build());
        }
    }
}
