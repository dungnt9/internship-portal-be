package com.dungnguyen.registration_service.controller;

import com.dungnguyen.registration_service.response.ApiResponse;
import com.dungnguyen.registration_service.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileUploadService fileUploadService;

    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * Download/View file
     * GET /files/view?path=/uploads/cv/student123/2024.2/CV_student123_2024.2_20240115_143022.pdf
     */
    @GetMapping("/view")
    public ResponseEntity<Resource> viewFile(@RequestParam String path) {
        try {
            // Validate and clean the path
            if (path == null || path.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Remove /uploads prefix if present
            String cleanPath = path.startsWith("/uploads/") ?
                    path.substring("/uploads/".length()) : path;

            // Construct file path
            Path filePath = Paths.get(uploadDir).resolve(cleanPath).normalize();

            // Security check: ensure the file is within the upload directory
            if (!filePath.startsWith(Paths.get(uploadDir))) {
                log.warn("Attempted to access file outside upload directory: {}", path);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine content type
                String contentType = determineContentType(cleanPath);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            log.error("Error accessing file: {}", path, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Download file with attachment disposition
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {
        try {
            // Similar logic to viewFile but with attachment disposition
            if (path == null || path.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String cleanPath = path.startsWith("/uploads/") ?
                    path.substring("/uploads/".length()) : path;

            Path filePath = Paths.get(uploadDir).resolve(cleanPath).normalize();

            if (!filePath.startsWith(Paths.get(uploadDir))) {
                log.warn("Attempted to access file outside upload directory: {}", path);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(cleanPath);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            log.error("Error downloading file: {}", path, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get file info
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<FileInfo>> getFileInfo(@RequestParam String path) {
        try {
            if (!fileUploadService.fileExists(path)) {
                return ResponseEntity.notFound().build();
            }

            FileInfo fileInfo = new FileInfo();
            fileInfo.setPath(path);
            fileInfo.setFileName(fileUploadService.getFileName(path));
            fileInfo.setSize(fileUploadService.getFileSize(path));
            fileInfo.setExists(true);

            return ResponseEntity.ok(ApiResponse.<FileInfo>builder()
                    .status(HttpStatus.OK.value())
                    .message("File info retrieved successfully")
                    .data(fileInfo)
                    .build());
        } catch (Exception e) {
            log.error("Error getting file info: {}", path, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<FileInfo>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error getting file info: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    private String determineContentType(String filePath) {
        String extension = getFileExtension(filePath).toLowerCase();

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    // Inner class for file info
    public static class FileInfo {
        private String path;
        private String fileName;
        private long size;
        private boolean exists;

        // Getters and setters
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        public boolean isExists() { return exists; }
        public void setExists(boolean exists) { this.exists = exists; }
    }
}