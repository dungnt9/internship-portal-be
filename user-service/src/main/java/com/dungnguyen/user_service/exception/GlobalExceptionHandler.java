package com.dungnguyen.user_service.exception;

import com.dungnguyen.user_service.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudentNotFoundException(StudentNotFoundException ex) {
        log.error("Student not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleTeacherNotFoundException(TeacherNotFoundException ex) {
        log.error("Teacher not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CompanyContactNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCompanyContactNotFoundException(CompanyContactNotFoundException ex) {
        log.error("Company contact not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAdminNotFoundException(AdminNotFoundException ex) {
        log.error("Admin not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("HTTP client error: {}", ex.getMessage());
        return createErrorResponse(ex.getStatusCode(), "Error communicating with service: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errors)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ApiResponse<Object>> createErrorResponse(HttpStatus status, String message) {
        ApiResponse<Object> response = ApiResponse.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<ApiResponse<Object>> createErrorResponse(org.springframework.http.HttpStatusCode status, String message) {
        ApiResponse<Object> response = ApiResponse.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return new ResponseEntity<>(response, status);
    }
}