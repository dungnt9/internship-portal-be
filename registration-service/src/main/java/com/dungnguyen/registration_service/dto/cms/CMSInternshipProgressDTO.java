package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipProgressDTO {
    private Integer id;
    private Integer studentId;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    private Integer positionId;
    private String positionTitle;
    private String periodId;
    private Integer teacherId;
    private String teacherName;
    private String teacherEmail;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isExternal;
    private Integer externalId;

    // For external internships
    private String companyName;
    private String externalPositionTitle; // To differentiate from position title

    // For internal internships
    private String internalCompanyName;

    private String status;
    private String supervisorName;
    private String supervisorPosition;
    private String supervisorEmail;
    private String supervisorPhone;
    private Boolean teacherConfirmed;
    private LocalDateTime teacherConfirmedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}