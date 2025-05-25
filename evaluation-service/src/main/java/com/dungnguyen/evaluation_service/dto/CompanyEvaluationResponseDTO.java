package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEvaluationResponseDTO {
    private Integer id;
    private Integer progressId;
    private LocalDateTime evaluationDate;
    private BigDecimal score;
    private String comments;
    private List<EvaluationDetailDTO> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
