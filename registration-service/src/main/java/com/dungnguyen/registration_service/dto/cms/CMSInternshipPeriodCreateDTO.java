package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipPeriodCreateDTO {
    private String id; // Format: YYYY.S (e.g., 2024.1, 2024.2)
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationStartDate;
    private LocalDate registrationEndDate;
    private String status; // UPCOMING, ACTIVE, END
    private String description;
}