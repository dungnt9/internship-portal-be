package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.InternshipPeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periods")
@RequiredArgsConstructor
@Slf4j
public class InternshipPeriodController {

    private final InternshipPeriodService periodService;

    /**
     * Get all active internship periods
     * This endpoint is accessible to admin role only
     *
     * @return List of InternshipPeriod
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipPeriod>>> getAllPeriods() {
        try {
            log.info("Getting all active internship periods");
            List<InternshipPeriod> periods = periodService.getAllActivePeriods();

            return ResponseEntity.ok(ApiResponse.<List<InternshipPeriod>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Internship periods retrieved successfully")
                    .data(periods)
                    .build());

        } catch (Exception e) {
            log.error("Error getting internship periods: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipPeriod>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving internship periods")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get current active internship period
     * This endpoint is accessible to all roles
     *
     * @return Current active InternshipPeriod
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<InternshipPeriod>> getCurrentPeriod() {
        try {
            log.info("Getting current active internship period");
            InternshipPeriod currentPeriod = periodService.getCurrentActivePeriod();

            return ResponseEntity.ok(ApiResponse.<InternshipPeriod>builder()
                    .status(HttpStatus.OK.value())
                    .message("Current internship period retrieved successfully")
                    .data(currentPeriod)
                    .build());

        } catch (Exception e) {
            log.error("Error getting current internship period: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipPeriod>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving current internship period")
                            .data(null)
                            .build());
        }
    }
}