package com.dungnguyen.registration_service.exception;

import com.dungnguyen.registration_service.response.ApiResponse;
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

    @ExceptionHandler(InternshipPositionNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleInternshipPositionNotFoundException(InternshipPositionNotFoundException ex) {
        log.error("Internship position not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InternshipPeriodNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleInternshipPeriodNotFoundException(InternshipPeriodNotFoundException ex) {
        log.error("Internship period not found: {}", ex.getMessage());
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