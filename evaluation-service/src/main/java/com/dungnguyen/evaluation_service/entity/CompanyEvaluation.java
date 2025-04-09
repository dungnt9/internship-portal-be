package com.dungnguyen.evaluation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "company_evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "progress_ref_id", nullable = false, unique = true)
    private ProgressReference progressRef;

    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;

    @Column(name = "overall_score", precision = 4, scale = 2)
    private BigDecimal overallScore;

    private String comments;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<CompanyEvaluationDetail> evaluationDetails;
}