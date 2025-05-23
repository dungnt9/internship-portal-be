package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipDTO;
import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipUpdateDTO;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.CMSExternalInternshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cms/external-internships")
@RequiredArgsConstructor
@Slf4j
public class CMSExternalInternshipController {

    private final CMSExternalInternshipService cmsExternalInternshipService;

    /**
     * Get all external internships
     *
     * @param authHeader Authorization header
     * @return List of CMSExternalInternshipDTO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSExternalInternshipDTO>>> getAllExternalInternships(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<CMSExternalInternshipDTO> externalInternships =
                    cmsExternalInternshipService.getAllExternalInternships(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<CMSExternalInternshipDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internships retrieved successfully")
                    .data(externalInternships)
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving external internships: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSExternalInternshipDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving external internships")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new external internship
     *
     * @param createDTO External internship creation data
     * @param confirmationFile Confirmation file
     * @param authHeader Authorization header
     * @return Created CMSExternalInternshipDTO
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CMSExternalInternshipDTO>> createExternalInternship(
            @RequestPart("data") CMSExternalInternshipCreateDTO createDTO,
            @RequestPart("confirmationFile") MultipartFile confirmationFile,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSExternalInternshipDTO createdExternalInternship =
                    cmsExternalInternshipService.createExternalInternship(createDTO, confirmationFile, authHeader);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("External internship created successfully")
                            .data(createdExternalInternship)
                            .build());
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating external internship: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Update external internship status (approve/reject)
     *
     * @param id External internship ID
     * @param updateDTO Update data containing new status
     * @param authHeader Authorization header
     * @return Updated CMSExternalInternshipDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSExternalInternshipDTO>> updateExternalInternship(
            @PathVariable Integer id,
            @RequestBody CMSExternalInternshipUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSExternalInternshipDTO updatedExternalInternship =
                    cmsExternalInternshipService.updateExternalInternship(id, updateDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<CMSExternalInternshipDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internship updated successfully")
                    .data(updatedExternalInternship)
                    .build());
        } catch (ExternalInternshipNotFoundException e) {
            log.error("External internship not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating external internship: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Update confirmation file for external internship
     *
     * @param id External internship ID
     * @param confirmationFile New confirmation file
     * @param authHeader Authorization header
     * @return Updated CMSExternalInternshipDTO
     */
    @PutMapping("/{id}/confirmation-file")
    public ResponseEntity<ApiResponse<CMSExternalInternshipDTO>> updateConfirmationFile(
            @PathVariable Integer id,
            @RequestParam("confirmationFile") MultipartFile confirmationFile,
            @RequestHeader("Authorization") String authHeader) {
        try {
            CMSExternalInternshipDTO updatedExternalInternship =
                    cmsExternalInternshipService.updateConfirmationFile(id, confirmationFile, authHeader);

            return ResponseEntity.ok(ApiResponse.<CMSExternalInternshipDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Confirmation file updated successfully")
                    .data(updatedExternalInternship)
                    .build());
        } catch (ExternalInternshipNotFoundException e) {
            log.error("External internship not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating confirmation file: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSExternalInternshipDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating confirmation file: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Delete external internship (soft delete)
     *
     * @param id External internship ID
     * @param authHeader Authorization header
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExternalInternship(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            cmsExternalInternshipService.deleteExternalInternship(id);

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internship deleted successfully")
                    .data(null)
                    .build());
        } catch (ExternalInternshipNotFoundException e) {
            log.error("External internship not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error deleting external internship: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}