package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.dto.InternshipApplicationCreateDTO;
import com.dungnguyen.registration_service.dto.InternshipApplicationDTO;
import com.dungnguyen.registration_service.dto.InternshipPreferencesRegisterDTO;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPositionNotFoundException;
import com.dungnguyen.registration_service.exception.ValidationException;
import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.InternshipApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
public class InternshipApplicationController {

    private final InternshipApplicationService internshipApplicationService;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;

    /**
     * Get current student's internship applications
     *
     * @param authHeader Authorization header
     * @return List of InternshipApplicationDTO
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<InternshipApplicationDTO>>> getMyApplications(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<InternshipApplicationDTO> applications = internshipApplicationService.getMyApplications(authHeader);

            return ResponseEntity.ok(ApiResponse.<List<InternshipApplicationDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Lấy thông tin đăng ký thực tập thành công")
                    .data(applications)
                    .build());
        } catch (Exception e) {
            log.error("Error getting internship applications: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<InternshipApplicationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi khi lấy thông tin đăng ký thực tập: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Create new internship application (upload CV)
     *
     * @param periodId Internship period ID
     * @param cvFile CV file
     * @param authHeader Authorization header
     * @return Created InternshipApplicationDTO
     */
    @PostMapping(value = "/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<InternshipApplicationDTO>> uploadCV(
            @RequestParam("periodId") String periodId,
            @RequestParam("cvFile") MultipartFile cvFile,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Get current student
            Integer studentId = authServiceClient.getUserStudentId(authHeader);
            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<InternshipApplicationDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Could not determine student from authorization token")
                                .data(null)
                                .build());
            }

            // Get student code
            StudentDTO student = userServiceClient.getStudentById(studentId, authHeader);
            String studentCode = student != null ? student.getStudentCode() : "student_" + studentId;

            InternshipApplicationCreateDTO createDTO = new InternshipApplicationCreateDTO(periodId);
            InternshipApplicationDTO createdApplication =
                    internshipApplicationService.createApplicationWithCV(createDTO, cvFile, studentCode, authHeader);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Tải lên CV thành công")
                            .data(createdApplication)
                            .build());
        } catch (Exception e) {
            log.error("Error uploading CV: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi khi tải lên CV: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * Register internship preferences (must have exactly 3 preferences)
     *
     * @param registerDTO Preferences data
     * @param authHeader Authorization header
     * @return Updated InternshipApplicationDTO
     */
    @PostMapping("/register-preferences")
    public ResponseEntity<ApiResponse<InternshipApplicationDTO>> registerPreferences(
            @RequestBody InternshipPreferencesRegisterDTO registerDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            InternshipApplicationDTO updatedApplication =
                    internshipApplicationService.registerPreferences(registerDTO, authHeader);

            return ResponseEntity.ok(ApiResponse.<InternshipApplicationDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Đăng ký nguyện vọng thực tập thành công")
                    .data(updatedApplication)
                    .build());
        } catch (InternshipApplicationNotFoundException e) {
            log.error("Application not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (InternshipPositionNotFoundException e) {
            log.error("Position not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (ValidationException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error registering preferences: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<InternshipApplicationDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi khi đăng ký nguyện vọng thực tập: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}