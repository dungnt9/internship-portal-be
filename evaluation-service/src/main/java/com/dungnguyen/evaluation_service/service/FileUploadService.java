package com.dungnguyen.evaluation_service.service;

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

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public String uploadReportFile(MultipartFile file, String studentCode, String periodId) {
        validateFile(file);

        try {
            String directoryPath = "reports/" + studentCode + "/" + periodId;
            Path directory = createDirectories(directoryPath);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Report_" + studentCode + "_" + periodId + "_" + timestamp + "." + fileExtension;

            Path filePath = directory.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Report file uploaded successfully: {}", filePath);
            return "/uploads/" + directoryPath + "/" + fileName;
        } catch (IOException e) {
            log.error("Failed to upload report file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload report file", e);
        }
    }

    public void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                String cleanPath = filePath.startsWith("/uploads/") ?
                        filePath.substring("/uploads/".length()) : filePath;

                Path fileToDelete = Paths.get(uploadDir, cleanPath);
                if (Files.exists(fileToDelete)) {
                    Files.delete(fileToDelete);
                    log.info("File deleted: {}", filePath);
                }
            }
        } catch (IOException ex) {
            log.error("Could not delete file: {}", filePath, ex);
        }
    }

    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        String cleanPath = filePath.startsWith("/uploads/") ?
                filePath.substring("/uploads/".length()) : filePath;

        Path file = Paths.get(uploadDir, cleanPath);
        return Files.exists(file);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Report file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("Report file size exceeds maximum allowed size (10MB)");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new RuntimeException("Invalid report filename");
        }

        String fileExtension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new RuntimeException("Invalid report file type. Allowed types: " +
                    String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    private Path createDirectories(String directoryPath) throws IOException {
        Path directory = Paths.get(uploadDir, directoryPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        return directory;
    }
}