package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.InternshipProgressDetailDTO;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/progress")
@RequiredArgsConstructor
@Slf4j
public class CMSProgressController {

    private final CMSProgressService cmsProgressService;

    /**
     * Get progress detail by ID for CMS
     */
    @GetMapping("/{progressId}")
    public ResponseEntity<ApiResponse<InternshipProgressDetailDTO>> getProgressDetail(
            @PathVariable Integer progressId) {
        try {
            InternshipProgressDetailDTO progressDetail = cmsProgressService.getProgressDetail(progressId);

            return ResponseEntity.ok(ApiResponse.<InternshipProgressDetailDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Progress detail retrieved successfully")
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
}