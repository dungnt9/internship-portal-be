package com.dungnguyen.registration_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    // Allowed file types
    private static final List<String> ALLOWED_CV_EXTENSIONS = Arrays.asList("pdf", "doc", "docx");
    private static final List<String> ALLOWED_CONFIRMATION_EXTENSIONS = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * Upload CV file for internship applications
     * Directory structure: uploads/cv/{studentCode}/{period}/
     */
    public String uploadCV(MultipartFile file, String studentCode, String periodId) {
        validateFile(file, ALLOWED_CV_EXTENSIONS, "CV");

        try {
            // Create directory structure
            String directoryPath = "cv/" + studentCode + "/" + periodId;
            Path directory = createDirectories(directoryPath);

            // Generate filename with timestamp to avoid conflicts
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "CV_" + studentCode + "_" + periodId + "_" + timestamp + "." + fileExtension;

            // Save the file
            Path filePath = directory.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("CV file uploaded successfully: {}", filePath);

            // Return relative path for database storage
            return "/uploads/" + directoryPath + "/" + fileName;
        } catch (IOException e) {
            log.error("Failed to upload CV file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload CV file", e);
        }
    }

    /**
     * Upload confirmation file for external internships
     * Directory structure: uploads/confirmations/{studentCode}/{period}/
     */
    public String uploadConfirmationFile(MultipartFile file, String studentCode, String periodId) {
        validateFile(file, ALLOWED_CONFIRMATION_EXTENSIONS, "Confirmation");

        try {
            // Create directory structure
            String directoryPath = "confirmations/" + studentCode + "/" + periodId;
            Path directory = createDirectories(directoryPath);

            // Generate filename with timestamp
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Confirmation_" + studentCode + "_" + periodId + "_" + timestamp + "." + fileExtension;

            // Save the file
            Path filePath = directory.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Confirmation file uploaded successfully: {}", filePath);

            // Return relative path for database storage
            return "/uploads/" + directoryPath + "/" + fileName;
        } catch (IOException e) {
            log.error("Failed to upload confirmation file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload confirmation file", e);
        }
    }

    /**
     * Generic upload method (keeping for backward compatibility)
     */
    public String uploadFile(MultipartFile file, String studentCode, String fileType) {
        if ("cv".equals(fileType)) {
            // For backward compatibility, use current period or generate a default
            String defaultPeriod = "current";
            return uploadCV(file, studentCode, defaultPeriod);
        } else if ("confirmations".equals(fileType)) {
            String defaultPeriod = "current";
            return uploadConfirmationFile(file, studentCode, defaultPeriod);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    /**
     * Delete file
     */
    public void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                // Remove /uploads prefix and construct full path
                String cleanPath = filePath.startsWith("/uploads/") ?
                        filePath.substring("/uploads/".length()) : filePath;

                Path fileToDelete = Paths.get(uploadDir, cleanPath);
                if (Files.exists(fileToDelete)) {
                    Files.delete(fileToDelete);
                    log.info("File deleted: {}", filePath);
                } else {
                    log.warn("File not found for deletion: {}", filePath);
                }
            }
        } catch (IOException ex) {
            log.error("Could not delete file: {}", filePath, ex);
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        String cleanPath = filePath.startsWith("/uploads/") ?
                filePath.substring("/uploads/".length()) : filePath;

        Path file = Paths.get(uploadDir, cleanPath);
        return Files.exists(file);
    }

    /**
     * Get file size
     */
    public long getFileSize(String filePath) {
        try {
            if (filePath == null || filePath.isEmpty()) {
                return 0;
            }

            String cleanPath = filePath.startsWith("/uploads/") ?
                    filePath.substring("/uploads/".length()) : filePath;

            Path file = Paths.get(uploadDir, cleanPath);
            return Files.exists(file) ? Files.size(file) : 0;
        } catch (IOException e) {
            log.error("Error getting file size for: {}", filePath, e);
            return 0;
        }
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file, List<String> allowedExtensions, String fileType) {
        if (file.isEmpty()) {
            throw new RuntimeException(fileType + " file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException(fileType + " file size exceeds maximum allowed size (10MB)");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new RuntimeException("Invalid " + fileType.toLowerCase() + " filename");
        }

        String fileExtension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!allowedExtensions.contains(fileExtension)) {
            throw new RuntimeException("Invalid " + fileType.toLowerCase() + " file type. Allowed types: " +
                    String.join(", ", allowedExtensions));
        }
    }

    /**
     * Create directories if they don't exist
     */
    private Path createDirectories(String directoryPath) throws IOException {
        Path directory = Paths.get(uploadDir, directoryPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        return directory;
    }

    /**
     * Get readable file name from path
     */
    public String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }

    /**
     * Get file URL for frontend access
     */
    public String getFileUrl(String filePath, String baseUrl) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        // Ensure filePath starts with /uploads/
        if (!filePath.startsWith("/uploads/")) {
            filePath = "/uploads/" + filePath;
        }

        return baseUrl + filePath;
    }
}