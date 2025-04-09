package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "external_internships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalInternship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_ref_id", nullable = false)
    private StudentReference studentRef;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "company_address", length = 255)
    private String companyAddress;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(name = "supervisor_name", length = 100)
    private String supervisorName;

    @Column(name = "supervisor_position", length = 100)
    private String supervisorPosition;

    @Column(name = "supervisor_email", length = 100)
    private String supervisorEmail;

    @Column(name = "supervisor_phone", length = 20)
    private String supervisorPhone;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "work_schedule")
    private String workSchedule;

    @Column(name = "confirmation_file_path", length = 255)
    private String confirmationFilePath;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "external", cascade = CascadeType.ALL)
    private List<InternshipProgress> progressList;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}