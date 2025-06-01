package com.dungnguyen.user_service.service;

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
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:D:/code/datn/internship-portal-be/uploads}")
    private String uploadDir;

    @Value("${server.port:8002}")
    private String serverPort;

    public String storeFile(MultipartFile file, String userType, Integer userId) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Check file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }

            // Create directory structure based on userType
            String subDir;
            if ("logo".equals(userType)) {
                subDir = "company"; // Logo của company nằm ở folder company
            } else {
                subDir = "avatars/" + userType.toLowerCase(); // Avatar nằm ở folder avatars/{userType}
            }

            Path uploadPath = Paths.get(uploadDir, subDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String fileName = userId + "_" + UUID.randomUUID().toString() + "." + fileExtension;

            // Store file
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Return relative path for database storage
            String relativePath = "/uploads/" + subDir + "/" + fileName;
            log.info("File stored successfully: {}", relativePath);

            return relativePath;

        } catch (IOException ex) {
            log.error("Could not store file. Error: ", ex);
            throw new RuntimeException("Could not store file. Error: " + ex.getMessage());
        }
    }

    public void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                // Remove /uploads prefix and construct full path
                String cleanPath = filePath.startsWith("/uploads/") ?
                        filePath.substring("/uploads/".length()) : filePath;

                Path fileToDelete = Paths.get(uploadDir, cleanPath);
                Files.deleteIfExists(fileToDelete);
                log.info("File deleted: {}", filePath);
            }
        } catch (IOException ex) {
            log.error("Could not delete file: {}", filePath, ex);
        }
    }
}