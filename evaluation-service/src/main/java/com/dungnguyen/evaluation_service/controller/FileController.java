package com.dungnguyen.evaluation_service.controller;

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

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping("/view")
    public ResponseEntity<Resource> viewFile(@RequestParam String path) {
        try {
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

    private String determineContentType(String filePath) {
        String extension = getFileExtension(filePath).toLowerCase();

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default:
                return "application/octet-stream";
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }
}