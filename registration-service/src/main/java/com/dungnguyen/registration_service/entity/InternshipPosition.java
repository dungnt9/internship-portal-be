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
@Table(name = "internship_positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_ref_id", nullable = false)
    private CompanyReference companyRef;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private InternshipPeriod period;

    @Column(nullable = false, length = 100)
    private String title;

    private String description;
    private String requirements;
    private String benefits;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots = 1;

    @Column(name = "registered_count", nullable = false)
    private Integer registeredCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_type")
    private WorkType workType = WorkType.FULL_TIME;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<InternshipApplication> applications;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<InternshipProgress> progressList;

    public enum WorkType {
        FULL_TIME("Full-time"),
        PART_TIME("Part-time");

        private String value;

        WorkType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}