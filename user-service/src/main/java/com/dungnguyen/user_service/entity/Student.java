package com.dungnguyen.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Integer authUserId;

    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(length = 100)
    private String major;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthday;

    @Column(length = 255)
    private String address;

    @Column(precision = 3, scale = 2)
    private BigDecimal cpa;

    @Column(name = "english_level", length = 100)
    private String englishLevel;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Gender {
        Male, Female, Other
    }
}