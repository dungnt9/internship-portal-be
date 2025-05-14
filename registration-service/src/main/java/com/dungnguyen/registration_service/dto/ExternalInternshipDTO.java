package com.dungnguyen.registration_service.dto;

import com.dungnguyen.registration_service.entity.ExternalInternship;
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

    // Period information
    private LocalDateTime periodStartDate;
    private LocalDateTime periodEndDate;
    private String periodDescription;

    // Constructor to convert from entity
    public ExternalInternshipDTO(ExternalInternship externalInternship) {
        this.id = externalInternship.getId();
        this.studentId = externalInternship.getStudentId();
        this.periodId = externalInternship.getPeriod().getId();
        this.confirmationFilePath = externalInternship.getConfirmationFilePath();
        this.status = externalInternship.getStatus().name();
        this.createdAt = externalInternship.getCreatedAt();
        this.updatedAt = externalInternship.getUpdatedAt();

        // Set period information
        if (externalInternship.getPeriod() != null) {
            this.periodStartDate = externalInternship.getPeriod().getStartDate().atStartOfDay();
            this.periodEndDate = externalInternship.getPeriod().getEndDate().atStartOfDay();
            this.periodDescription = externalInternship.getPeriod().getDescription();
        }
    }
}