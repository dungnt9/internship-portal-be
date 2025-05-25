package com.dungnguyen.evaluation_service.dto;

import com.dungnguyen.evaluation_service.entity.InternshipReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipReportDTO {
    private Integer id;
    private Integer progressId;
    private String title;
    private String content;
    private String filePath;
    private LocalDateTime submissionDate;
    private Boolean isEditable; // NEW FIELD
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InternshipReportDTO(InternshipReport report) {
        this.id = report.getId();
        this.progressId = report.getProgressId();
        this.title = report.getTitle();
        this.content = report.getContent();
        this.filePath = report.getFilePath();
        this.submissionDate = report.getSubmissionDate();
        this.createdAt = report.getCreatedAt();
        this.updatedAt = report.getUpdatedAt();

        // Calculate if report is editable
        this.isEditable = calculateIsEditable(report);
    }

    private Boolean calculateIsEditable(InternshipReport report) {
        // Report is NOT editable if it has been submitted
        // (has submission date AND both title and content are filled)
        return !(report.getSubmissionDate() != null &&
                report.getTitle() != null && !report.getTitle().trim().isEmpty() &&
                report.getContent() != null && !report.getContent().trim().isEmpty());
    }
}