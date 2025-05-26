package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInternshipEvaluationDTO {
    private Integer progressId;
    private String studentCode;
    private String studentName;
    private String positionTitle;
    private String startDate;
    private String endDate;
    private String status;
    private String evaluationStatus; // PENDING, COMPLETED
    private BigDecimal score;
    private LocalDateTime evaluationDate;
}