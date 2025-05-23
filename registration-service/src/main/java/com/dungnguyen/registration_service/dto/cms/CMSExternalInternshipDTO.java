package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMSExternalInternshipDTO {
    private Integer id;
    private Integer studentId;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String periodId;
    private String confirmationFilePath;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}