package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipPositionDTO;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSInternshipPositionService;
import com.dungnguyen.registration_service.service.InternshipPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions/admin") // Fix typo: amin -> admin
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipPositionController {

    private final CMSInternshipPositionService cmsInternshipPositionService;
    private final InternshipPositionService internshipPositionService;

    /**
     * Get all positions with company details (for admin dropdown)
     * This endpoint is accessible to admin role only
     *
     * @param authHeader Authorization header
     * @return List of InternshipPositionDTO with company details
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InternshipPositionDTO>>> getAllPositions(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            log.info("Getting all internship positions");
            List<InternshipPositionDTO> positions = internshipPositionService.getAllPositionsWithCompanyDetails(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipPositionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship positions retrieved successfully")
                    .data(positions)
                    .build());

        } catch (Exception e) {
            log.error("Error getting internship positions: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship positions")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get positions by period ID
     * This endpoint is accessible to admin role only
     *
     * @param periodId   Period ID
     * @param authHeader Authorization header
     * @return List of InternshipPositionDTO with company details for the specified period
     */
    @GetMapping("/period/{periodId}")
    public ResponseEntity<ApiResponse<List<InternshipPositionDTO>>> getPositionsByPeriod(
            @PathVariable String periodId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            log.info("Getting internship positions for period: {}", periodId);
            List<InternshipPositionDTO> positions = cmsInternshipPositionService.getPositionsByPeriod(periodId, authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipPositionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship positions retrieved successfully")
                    .data(positions)
                    .build());

        } catch (Exception e) {
            log.error("Error getting internship positions for period {}: {}", periodId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPositionDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship positions")
                            .data(null)
                            .build());
        }
    }
}