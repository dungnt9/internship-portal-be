package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPositionCreateDTO {
    private String periodId;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private Integer availableSlots;
    private String workType;
    private LocalDate dueDate;
}