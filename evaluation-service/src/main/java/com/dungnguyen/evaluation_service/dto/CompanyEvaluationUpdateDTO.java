package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEvaluationUpdateDTO {
    private BigDecimal score;
    private String comments;
    private List<EvaluationCriteriaUpdateDTO> criteriaDetails;
}