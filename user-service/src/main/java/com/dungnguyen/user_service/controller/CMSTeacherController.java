package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.cms.CMSTeacherCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSTeacherDTO;
import com.dungnguyen.user_service.dto.cms.CMSTeacherUpdateDTO;
import com.dungnguyen.user_service.exception.TeacherNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CMSTeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/teachers")
@RequiredArgsConstructor
@Slf4j
public class CMSTeacherController {

    private final CMSTeacherService cmsTeacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSTeacherDTO>>> getAllTeachers() {
        try {
            List<CMSTeacherDTO> teachers = cmsTeacherService.getAllTeachers();
            return ResponseEntity.ok(ApiResponse.success(teachers, "Teachers retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all teachers: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSTeacherDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving teachers")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSTeacherDTO>> getTeacherById(@PathVariable Integer id) {
        try {
            CMSTeacherDTO teacher = cmsTeacherService.getTeacherById(id);
            return ResponseEntity.ok(ApiResponse.success(teacher, "Teacher retrieved successfully"));
        } catch (TeacherNotFoundException e) {
            log.error("Teacher not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving teacher: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the teacher")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSTeacherDTO>> createTeacher(@RequestBody CMSTeacherCreateDTO createDTO) {
        try {
            CMSTeacherDTO createdTeacher = cmsTeacherService.createTeacher(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdTeacher, "Teacher created successfully"));
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating teacher: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating teacher: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSTeacherDTO>> updateTeacher(
            @PathVariable Integer id,
            @RequestBody CMSTeacherUpdateDTO updateDTO) {
        try {
            CMSTeacherDTO updatedTeacher = cmsTeacherService.updateTeacher(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedTeacher, "Teacher updated successfully"));
        } catch (TeacherNotFoundException e) {
            log.error("Teacher not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating teacher: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSTeacherDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating teacher: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}