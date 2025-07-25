package com.dungnguyen.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", length = 150)
    private String shortName;

    @Column(name = "is_foreign_company")
    private Boolean isForeignCompany = false;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String address;

    @Column(name = "business_type", length = 100)
    private String businessType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "capital", precision = 20, scale = 2)
    private BigDecimal capital;

    @Column(name = "logo_path", length = 255)
    private String logoPath;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @Column(name = "is_linked")
    private Boolean isLinked = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}