package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class ImageUploadController {

    private final FileStorageService fileStorageService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CompanyContactService companyContactService;
    private final AdminService adminService;
    private final CompanyService companyService;

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user info
            Integer authUserId = getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Determine user type and update avatar
            String userType = determineUserType(authUserId, authHeader);
            String imagePath = fileStorageService.storeFile(file, userType, authUserId);

            // Update avatar path in database based on user type
            updateAvatarPath(authUserId, userType, imagePath);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .message("Avatar uploaded successfully")
                    .data(imagePath)
                    .build());

        } catch (Exception e) {
            log.error("Error uploading avatar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<String>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error uploading avatar: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    private Integer getCurrentUserAuthId(String authHeader) {
        try {
            return studentService.getCurrentUserAuthId(authHeader);
        } catch (Exception e) {
            return null;
        }
    }

    private String determineUserType(Integer authUserId, String authHeader) {
        try {
            studentService.getStudentByAuthUserId(authUserId, authHeader);
            return "student";
        } catch (Exception e1) {
            try {
                teacherService.getTeacherByAuthUserId(authUserId, authHeader);
                return "teacher";
            } catch (Exception e2) {
                try {
                    companyContactService.getCompanyContactByAuthUserId(authUserId, authHeader);
                    return "company-contact";
                } catch (Exception e3) {
                    try {
                        adminService.getAdminByAuthUserId(authUserId, authHeader);
                        return "admin";
                    } catch (Exception e4) {
                        throw new RuntimeException("User type not found");
                    }
                }
            }
        }
    }

    private void updateAvatarPath(Integer authUserId, String userType, String imagePath) {
        switch (userType) {
            case "student":
                studentService.updateAvatarPath(authUserId, imagePath);
                break;
            case "teacher":
                teacherService.updateAvatarPath(authUserId, imagePath);
                break;
            case "company-contact":
                companyContactService.updateAvatarPath(authUserId, imagePath);
                break;
            case "admin":
                adminService.updateAvatarPath(authUserId, imagePath);
                break;
            default:
                throw new RuntimeException("Invalid user type");
        }
    }

    @PostMapping("/logo")
    public ResponseEntity<ApiResponse<String>> uploadLogo(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user info
            Integer authUserId = getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Verify user is company contact
            try {
                companyContactService.getCompanyContactByAuthUserId(authUserId, authHeader);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.<String>builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .message("Only company contacts can upload company logos")
                                .data(null)
                                .build());
            }

            // Store logo file
            String logoPath = fileStorageService.storeFile(file, "logo", authUserId);

            // Update logo path in company
            companyService.updateCompanyLogo(authUserId, logoPath);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .message("Logo uploaded successfully")
                    .data(logoPath)
                    .build());

        } catch (Exception e) {
            log.error("Error uploading logo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<String>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error uploading logo: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}