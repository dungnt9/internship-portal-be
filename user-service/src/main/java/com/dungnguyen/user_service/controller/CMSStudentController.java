package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.cms.CMSStudentCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSStudentDTO;
import com.dungnguyen.user_service.dto.cms.CMSStudentUpdateDTO;
import com.dungnguyen.user_service.exception.StudentNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CMSStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/students")
@RequiredArgsConstructor
@Slf4j
public class CMSStudentController {

    private final CMSStudentService cmsStudentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSStudentDTO>>> getAllStudents() {
        try {
            List<CMSStudentDTO> students = cmsStudentService.getAllStudents();
            return ResponseEntity.ok(ApiResponse.success(students, "Students retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all students: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSStudentDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving students")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSStudentDTO>> getStudentById(@PathVariable Integer id) {
        try {
            CMSStudentDTO student = cmsStudentService.getStudentById(id);
            return ResponseEntity.ok(ApiResponse.success(student, "Student retrieved successfully"));
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving student: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the student")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSStudentDTO>> createStudent(@RequestBody CMSStudentCreateDTO createDTO) {
        try {
            CMSStudentDTO createdStudent = cmsStudentService.createStudent(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdStudent, "Student created successfully"));
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating student: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating student: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSStudentDTO>> updateStudent(
            @PathVariable Integer id,
            @RequestBody CMSStudentUpdateDTO updateDTO) {
        try {
            CMSStudentDTO updatedStudent = cmsStudentService.updateStudent(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedStudent, "Student updated successfully"));
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating student: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSStudentDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating student: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}