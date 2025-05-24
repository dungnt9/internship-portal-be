package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipProgressCreateDTO {
    private Integer studentId;
    private Integer positionId; // null for external internships
    private String periodId;
    private Integer teacherId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isExternal = false;
    private Integer externalId; // null for internal internships

    // For external internships
    private String companyName;
    private String positionTitle;

    private String status; // IN_PROGRESS, COMPLETED, CANCELLED
    private String supervisorName;
    private String supervisorPosition;
    private String supervisorEmail;
    private String supervisorPhone;
}