package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.ExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipUpdateDTO;
import com.dungnguyen.registration_service.exception.DuplicateExternalInternshipException;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.ExternalInternshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external-internships")
@RequiredArgsConstructor
@Slf4j
public class ExternalInternshipController {

    private final ExternalInternshipService externalInternshipService;

    /**
     * Get all external internships for the current student
     *
     * @param authHeader Authorization header
     * @return List of ExternalInternshipDTO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExternalInternshipDTO>>> getMyExternalInternships(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<ExternalInternshipDTO>>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            List<ExternalInternshipDTO> externalInternships = externalInternshipService.getMyExternalInternships(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<ExternalInternshipDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internships retrieved successfully")
                    .data(externalInternships)
                    .build());

        } catch (Exception e) {
            log.error("Error getting external internships: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ExternalInternshipDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving external internships")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get a specific external internship for the current student
     *
     * @param id         External internship ID
     * @param authHeader Authorization header
     * @return ExternalInternshipDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExternalInternshipDTO>> getMyExternalInternshipById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<ExternalInternshipDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            ExternalInternshipDTO externalInternship = externalInternshipService.getMyExternalInternshipById(id, authHeader);

            return ResponseEntity.ok(ApiResponse.<ExternalInternshipDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internship retrieved successfully")
                    .data(externalInternship)
                    .build());

        } catch (ExternalInternshipNotFoundException e) {
            log.error("External internship not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error getting external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the external internship")
                            .data(null)
                            .build());
        }
    }

    /**
     * Create a new external internship for the current student
     *
     * @param createDTO  External internship creation data
     * @param authHeader Authorization header
     * @return Created ExternalInternshipDTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ExternalInternshipDTO>> createExternalInternship(
            @RequestBody ExternalInternshipCreateDTO createDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<ExternalInternshipDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            ExternalInternshipDTO createdExternalInternship = externalInternshipService.createExternalInternship(createDTO, authHeader);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("External internship created successfully")
                            .data(createdExternalInternship)
                            .build());

        } catch (DuplicateExternalInternshipException e) {
            log.error("Duplicate external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating the external internship")
                            .data(null)
                            .build());
        }
    }

    /**
     * Update an existing external internship for the current student
     *
     * @param id         External internship ID
     * @param updateDTO  External internship update data
     * @param authHeader Authorization header
     * @return Updated ExternalInternshipDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExternalInternshipDTO>> updateExternalInternship(
            @PathVariable Integer id,
            @RequestBody ExternalInternshipUpdateDTO updateDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<ExternalInternshipDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            ExternalInternshipDTO updatedExternalInternship = externalInternshipService.updateExternalInternship(id, updateDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<ExternalInternshipDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("External internship updated successfully")
                    .data(updatedExternalInternship)
                    .build());

        } catch (ExternalInternshipNotFoundException e) {
            log.error("External internship not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the external internship")
                            .data(null)
                            .build());
        }
    }
}