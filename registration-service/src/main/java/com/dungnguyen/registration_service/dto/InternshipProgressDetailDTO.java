package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for detailed information about an internship progress,
 * including student, company, and position details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgressDetailDTO {
    // Internship progress data
    private InternshipProgressDTO progress;

    // Student information - enriched from the base progress dto
    private StudentDTO student;

    // Company information
    private CompanyDTO company;

    // Position information (may be null for external internships)
    private InternshipPositionDTO position;
}