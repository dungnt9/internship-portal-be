package com.dungnguyen.registration_service.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgressCreatedMessage {
    private Integer progressId;
    private Integer studentId;
    private Integer positionId;
    private String periodId;
    private Integer teacherId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isExternal;
    private Integer externalId;
    private String externalCompanyName;
    private String externalPositionTitle;
    private LocalDateTime createdAt;
}