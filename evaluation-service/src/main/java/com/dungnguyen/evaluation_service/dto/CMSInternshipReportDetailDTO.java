package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipReportDetailDTO {
    private Integer id;
    private Integer progressId;
    private String title;
    private String content;
    private String filePath;
    private LocalDateTime submissionDate;
    private Boolean isEditable;

    // Student info
    private Integer studentId;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    // Progress info
    private String periodId;
    private String positionTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private Boolean isExternal;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}