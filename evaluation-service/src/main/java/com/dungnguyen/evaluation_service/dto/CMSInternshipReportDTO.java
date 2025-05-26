package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipReportDTO {
    private Integer id;
    private Integer progressId;
    private String title;
    private String filePath;
    private LocalDateTime submissionDate;
    private Boolean isEditable;

    // Student info
    private Integer studentId;
    private String studentCode;
    private String studentName;

    // Progress info
    private String periodId;
    private String positionTitle;
    private String companyName;
    private Boolean isExternal;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}