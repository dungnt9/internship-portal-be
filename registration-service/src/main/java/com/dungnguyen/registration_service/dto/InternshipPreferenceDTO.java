package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPreferenceDTO {
    private Integer positionId;
    private Integer preferenceOrder; // 1, 2, or 3
    private String note;
}