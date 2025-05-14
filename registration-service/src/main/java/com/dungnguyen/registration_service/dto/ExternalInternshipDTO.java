package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalInternshipDTO {
    private Integer id;
    private Integer studentId;
    private String periodId;
    private String confirmationFilePath;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}