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
@Table(name = "internship_periods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPeriod {
    @Id
    @Column(length = 10)
    private String id; // Format: YYYY.S (e.g., 2024.1, 2024.2)

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "registration_start_date", nullable = false)
    private LocalDate registrationStartDate;

    @Column(name = "registration_end_date", nullable = false)
    private LocalDate registrationEndDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UPCOMING;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        UPCOMING("Chưa bắt đầu"),
        ACTIVE("Đang diễn ra"),
        END("Kết thúc");

        private String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}