package com.dungnguyen.registration_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String studentCode, String fileType) {
        try {
            // Create directories if they don't exist
            String directoryPath = uploadDir + "/" + fileType + "/" + studentCode;
            Path directory = Paths.get(directoryPath);
            Files.createDirectories(directory);

            // Keep original filename, add suffix only if file exists
            String originalFilename = file.getOriginalFilename();
            String newFilename = originalFilename;

            // Check if file already exists and add timestamp suffix if needed
            Path filePath = directory.resolve(newFilename);
            if (Files.exists(filePath)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                newFilename = nameWithoutExt + "_" + timestamp + fileExtension;
            }

            // Save the file
            filePath = directory.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File uploaded successfully: {}", filePath);

            // Return relative path for storage in the database
            return "/uploads/" + fileType + "/" + studentCode + "/" + newFilename;
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}