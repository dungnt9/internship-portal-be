package com.dungnguyen.evaluation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}