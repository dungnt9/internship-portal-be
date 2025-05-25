package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDetailDTO {
    private Integer id;
    private String criteriaName;
    private String criteriaDescription;
    private String comments;
}