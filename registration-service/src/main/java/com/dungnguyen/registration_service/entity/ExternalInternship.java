package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "external_internships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalInternship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @Column(name = "confirmation_file_path", length = 255)
    private String confirmationFilePath;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}