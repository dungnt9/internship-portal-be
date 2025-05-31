package com.dungnguyen.evaluation_service.exception;

import com.dungnguyen.evaluation_service.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle evaluation criteria not found exception
     */
    @ExceptionHandler(EvaluationCriteriaNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEvaluationCriteriaNotFoundException(EvaluationCriteriaNotFoundException ex) {
        log.error("Evaluation criteria not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle internship evaluation not found exception
     */
    @ExceptionHandler(InternshipEvaluationNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleInternshipEvaluationNotFoundException(InternshipEvaluationNotFoundException ex) {
        log.error("Internship evaluation not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle internship report not found exception
     */
    @ExceptionHandler(InternshipReportNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleInternshipReportNotFoundException(InternshipReportNotFoundException ex) {
        log.error("Internship report not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle report already submitted exception
     */
    @ExceptionHandler(ReportAlreadySubmittedException.class)
    public ResponseEntity<ApiResponse<Object>> handleReportAlreadySubmittedException(ReportAlreadySubmittedException ex) {
        log.error("Report already submitted: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handle unauthorized access exception
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        log.error("Unauthorized access: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Handle illegal argument exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handle illegal state exception
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handle file upload related runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileUploadException(RuntimeException ex) {
        if (ex.getMessage().contains("file") || ex.getMessage().contains("File") ||
                ex.getMessage().contains("upload") || ex.getMessage().contains("Upload")) {
            log.error("File operation error: {}", ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return handleGlobalException(ex);
    }

    /**
     * Handle file size exceeded exception
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.error("File size exceeded: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "File size exceeds maximum allowed size (10MB)");
    }

    /**
     * Handle IO exceptions
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Object>> handleIOException(IOException ex) {
        log.error("IO error: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File operation failed");
    }

    /**
     * Handle file access denied exceptions
     */
    @ExceptionHandler(java.nio.file.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileAccessException(java.nio.file.AccessDeniedException ex) {
        log.error("File access denied: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.FORBIDDEN, "Access denied to file");
    }

    /**
     * Handle generic runtime exceptions
     */
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(RuntimeException ex) {
        log.error("Unexpected runtime error: {}", ex.getMessage(), ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Helper method to create error response
     */
    private ResponseEntity<ApiResponse<Object>> createErrorResponse(HttpStatus status, String message) {
        ApiResponse<Object> response = ApiResponse.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}