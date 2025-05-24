package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.TeacherDTO;
import com.dungnguyen.user_service.dto.TeacherUpdateDTO;
import com.dungnguyen.user_service.exception.TeacherNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<TeacherDTO>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<TeacherDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = teacherService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<TeacherDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Get teacher profile
            TeacherDTO teacherDTO = teacherService.getTeacherByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<TeacherDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Teacher profile retrieved successfully")
                    .data(teacherDTO)
                    .build());

        } catch (TeacherNotFoundException e) {
            log.error("Teacher not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving teacher profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the teacher profile")
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<TeacherDTO>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TeacherUpdateDTO updateDTO) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<TeacherDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = teacherService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<TeacherDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Update teacher profile
            TeacherDTO updatedTeacher = teacherService.updateTeacher(authUserId, updateDTO);

            // Add auth information to the response
            TeacherDTO fullTeacherDTO = teacherService.getTeacherByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<TeacherDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Teacher profile updated successfully")
                    .data(fullTeacherDTO)
                    .build());

        } catch (TeacherNotFoundException e) {
            log.error("Teacher not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating teacher profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the teacher profile")
                            .data(null)
                            .build());
        }
    }

    /**
     * Get teacher by ID (for other services)
     *
     * @param id Teacher ID
     * @param authHeader Authorization header
     * @return TeacherDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherDTO>> getTeacherById(
            @PathVariable Integer id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            TeacherDTO teacherDTO = teacherService.getTeacherById(id, authHeader);

            return ResponseEntity.ok(ApiResponse.<TeacherDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Teacher retrieved successfully")
                    .data(teacherDTO)
                    .build());

        } catch (TeacherNotFoundException e) {
            log.error("Teacher not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving teacher: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<TeacherDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the teacher")
                            .data(null)
                            .build());
        }
    }
}