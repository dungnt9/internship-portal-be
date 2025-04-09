package com.dungnguyen.evaluation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "progress_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "original_progress_id", nullable = false, unique = true)
    private Integer originalProgressId;

    @Column(name = "student_name", nullable = false, length = 100)
    private String studentName;

    @Column(name = "period_name", nullable = false, length = 100)
    private String periodName;

    @Column(name = "position_title", length = 100)
    private String positionTitle;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "progressRef", cascade = CascadeType.ALL)
    private List<InternshipReport> reports;

    @OneToOne(mappedBy = "progressRef", cascade = CascadeType.ALL)
    private CompanyEvaluation companyEvaluation;
}