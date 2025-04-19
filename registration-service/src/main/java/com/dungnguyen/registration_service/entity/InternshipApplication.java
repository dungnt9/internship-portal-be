package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private InternshipPosition position;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @Column(name = "preference_order", nullable = false)
    private Integer preferenceOrder;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "applied_date", updatable = false)
    private LocalDateTime appliedDate;

    @Column(name = "cv_id")
    private Integer cvId;

    @Column(columnDefinition = "TEXT")
    private String note;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}