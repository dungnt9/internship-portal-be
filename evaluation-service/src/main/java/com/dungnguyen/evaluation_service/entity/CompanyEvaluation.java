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
@Table(name = "company_evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "progress_id", nullable = false, unique = true)
    private Integer progressId;

    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;

    @Column(name = "score", precision = 4, scale = 2)
    private BigDecimal score;

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