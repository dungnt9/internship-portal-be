package com.dungnguyen.evaluation_service.dto;

import com.dungnguyen.evaluation_service.entity.EvaluationCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriteriaDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EvaluationCriteriaDTO(EvaluationCriteria criteria) {
        this.id = criteria.getId();
        this.name = criteria.getName();
        this.description = criteria.getDescription();
        this.createdAt = criteria.getCreatedAt();
        this.updatedAt = criteria.getUpdatedAt();
    }
}