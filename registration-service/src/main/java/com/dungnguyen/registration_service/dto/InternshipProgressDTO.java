package com.dungnguyen.registration_service.dto;

import com.dungnguyen.registration_service.entity.InternshipProgress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgressDTO {
    private Integer id;
    private Integer studentId;
    private Integer positionId;
    private String periodId;
    private String positionTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isExternal;
    private String status;
    private String companyName;
    private String supervisorName;
    private String supervisorPosition;
    private String supervisorEmail;
    private String supervisorPhone;
    private Boolean teacherConfirmed;
    private LocalDateTime teacherConfirmedAt;

    // Constructor to convert from entity
    public InternshipProgressDTO(InternshipProgress progress) {
        this.id = progress.getId();
        this.studentId = progress.getStudentId();
        this.positionId = progress.getPosition() != null ? progress.getPosition().getId() : null;
        this.periodId = progress.getPeriod().getId();

        if (progress.getIsExternal()) {
            // For external internships, use the explicitly set positionTitle
            this.positionTitle = progress.getPositionTitle();
            // For external internships, use the explicitly set companyName
            this.companyName = progress.getCompanyName();
        } else {
            // For internal internships, get title from position
            this.positionTitle = progress.getPosition() != null ? progress.getPosition().getTitle() : null;
            // For internal internships, company name is null in this DTO (it will be populated from position elsewhere)
            this.companyName = null;
        }

        this.startDate = progress.getStartDate();
        this.endDate = progress.getEndDate();
        this.isExternal = progress.getIsExternal();
        this.status = progress.getStatus().name();
        this.supervisorName = progress.getSupervisorName();
        this.supervisorPosition = progress.getSupervisorPosition();
        this.supervisorEmail = progress.getSupervisorEmail();
        this.supervisorPhone = progress.getSupervisorPhone();
        this.teacherConfirmed = progress.getTeacherConfirmed();
        this.teacherConfirmedAt = progress.getTeacherConfirmedAt();
    }
}