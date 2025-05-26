package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriteriaDetailDTO {
    private Integer id;
    private Integer criteriaId;
    private String criteriaName;
    private String criteriaDescription;
    private String comments;
}
