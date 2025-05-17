package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.dto.ExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipDTO;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.ExternalInternshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/external-internships")
@RequiredArgsConstructor
@Slf4j
public class ExternalInternshipController {

    private final ExternalInternshipService externalInternshipService;

    /**
     * Get all external internships for current student in current period
     *
     * @param authHeader Authorization header
     * @return List of ExternalInternshipDTO
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ExternalInternshipDTO>>> getMyExternalInternships(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<ExternalInternshipDTO> externalInternships = externalInternshipService.getMyExternalInternships(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<ExternalInternshipDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Lấy thông tin đăng ký thực tập ngoài trường thành công")
                    .data(externalInternships)
                    .build());
        } catch (InternshipPeriodNotFoundException e) {
            log.error("No active period found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<List<ExternalInternshipDTO>>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Không có kỳ thực tập đang hoạt động")
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error getting external internships: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ExternalInternshipDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi khi lấy thông tin đăng ký thực tập ngoài trường: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new external internship application
     *
     * @param periodId Period ID
     * @param confirmationFile CV/Confirmation letter file
     * @param authHeader Authorization header
     * @return Created ExternalInternshipDTO
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ExternalInternshipDTO>> createExternalInternship(
            @RequestParam("periodId") String periodId,
            @RequestParam("confirmationFile") MultipartFile confirmationFile,
            @RequestHeader("Authorization") String authHeader) {
        try {
            ExternalInternshipCreateDTO createDTO = new ExternalInternshipCreateDTO(periodId);
            ExternalInternshipDTO createdExternalInternship =
                    externalInternshipService.createExternalInternship(createDTO, confirmationFile, authHeader);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Đăng ký thực tập ngoài trường thành công")
                            .data(createdExternalInternship)
                            .build());
        } catch (InternshipPeriodNotFoundException e) {
            log.error("Period not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating external internship: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ExternalInternshipDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi khi tạo đăng ký thực tập ngoài trường: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}