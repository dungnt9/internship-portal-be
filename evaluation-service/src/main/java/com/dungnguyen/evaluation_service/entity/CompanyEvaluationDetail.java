package com.dungnguyen.evaluation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "company_evaluation_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEvaluationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    private CompanyEvaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    private EvaluationCriteria criteria;

    @Column(precision = 4, scale = 2)
    private BigDecimal score;

    private String comments;
}