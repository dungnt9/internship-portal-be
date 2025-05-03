package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.StudentDTO;
import com.dungnguyen.user_service.dto.StudentUpdateDTO;
import com.dungnguyen.user_service.exception.StudentNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentDTO>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<StudentDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = studentService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<StudentDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Get student profile
            StudentDTO studentDTO = studentService.getStudentByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<StudentDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Student profile retrieved successfully")
                    .data(studentDTO)
                    .build());

        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<StudentDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving student profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<StudentDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the student profile")
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<StudentDTO>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody StudentUpdateDTO updateDTO) {
        try {
            // Extract token from authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<StudentDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Authorization header is missing or invalid")
                                .data(null)
                                .build());
            }

            // Get current user's auth ID from token
            Integer authUserId = studentService.getCurrentUserAuthId(authHeader);
            if (authUserId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<StudentDTO>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid or expired token")
                                .data(null)
                                .build());
            }

            // Update student profile
            StudentDTO updatedStudent = studentService.updateStudent(authUserId, updateDTO);

            // Add auth information to the response
            StudentDTO fullStudentDTO = studentService.getStudentByAuthUserId(authUserId, authHeader);

            return ResponseEntity.ok(ApiResponse.<StudentDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Student profile updated successfully")
                    .data(fullStudentDTO)
                    .build());

        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<StudentDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating student profile: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<StudentDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while updating the student profile")
                            .data(null)
                            .build());
        }
    }
}