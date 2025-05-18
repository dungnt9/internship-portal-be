package com.dungnguyen.registration_service.dto;

import com.dungnguyen.registration_service.entity.InternshipPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPositionDTO {
    private Integer id;
    private Integer companyId;
    private String periodId;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private Integer availableSlots;
    private String workType;
    private String status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Company information
    private String companyName;
    private String companyShortName;
    private String website;
    private String logoPath;
    private String address;
    private String businessType;

    // Constructor to convert from entity (without company details)
    public InternshipPositionDTO(InternshipPosition position) {
        if (position != null) {
            this.id = position.getId();
            this.companyId = position.getCompanyId();
            this.periodId = position.getPeriod().getId();
            this.title = position.getTitle();
            this.description = position.getDescription();
            this.requirements = position.getRequirements();
            this.benefits = position.getBenefits();
            this.availableSlots = position.getAvailableSlots();
            this.workType = position.getWorkType().getValue();
            this.status = position.getStatus().name();
            this.dueDate = position.getDueDate();
            this.createdAt = position.getCreatedAt();
            this.updatedAt = position.getUpdatedAt();
        }
    }
}