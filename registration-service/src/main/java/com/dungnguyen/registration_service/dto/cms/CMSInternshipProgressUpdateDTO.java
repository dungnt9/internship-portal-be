package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipProgressUpdateDTO {
    // Basic fields that can be updated
    private Integer teacherId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // IN_PROGRESS, COMPLETED, CANCELLED

    // For external internships only - company info can be updated
    private String companyName;
    private String positionTitle;

    // Supervisor information can be updated
    private String supervisorName;
    private String supervisorPosition;
    private String supervisorEmail;
    private String supervisorPhone;

    // Teacher confirmation
    private Boolean teacherConfirmed;

    // NOTE: The following fields CANNOT be updated for data integrity:
    // - studentId (cannot change student)
    // - periodId (cannot change period)
    // - isExternal (cannot change internship type)
    // - positionId (cannot change position for internal internships)
    // - externalId (cannot change external internship reference)
}