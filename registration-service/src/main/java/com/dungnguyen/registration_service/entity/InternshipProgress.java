package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private InternshipPosition position;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_external")
    private Boolean isExternal = false;

    @ManyToOne
    @JoinColumn(name = "external_id")
    private ExternalInternship external;

    // New fields for external internships
    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "position_title", length = 100)
    private String positionTitle;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    @Column(name = "supervisor_name", length = 100)
    private String supervisorName;

    @Column(name = "supervisor_position", length = 100)
    private String supervisorPosition;

    @Column(name = "supervisor_email", length = 100)
    private String supervisorEmail;

    @Column(name = "supervisor_phone", length = 20)
    private String supervisorPhone;

    @Column(name = "teacher_confirmed")
    private Boolean teacherConfirmed = false;

    @Column(name = "teacher_confirmed_at")
    private LocalDateTime teacherConfirmedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        IN_PROGRESS, COMPLETED, CANCELLED
    }
}