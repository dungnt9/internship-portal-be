package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgressUpdateDTO {
    // Common fields for both internal and external internships
    private String supervisorName;
    private String supervisorPosition;
    private String supervisorEmail;
    private String supervisorPhone;

    // Fields specifically for external internships
    private String companyName;
    private String positionTitle;
}