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

    @ManyToOne
    @JoinColumn(name = "student_ref_id", nullable = false)
    private StudentReference studentRef;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private InternshipPosition position;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @ManyToOne
    @JoinColumn(name = "teacher_ref_id")
    private TeacherReference teacherRef;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_external")
    private Boolean isExternal = false;

    @ManyToOne
    @JoinColumn(name = "external_id")
    private ExternalInternship external;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ASSIGNED;

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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}