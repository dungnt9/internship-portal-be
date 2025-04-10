package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "company_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id", nullable = false, unique = true)
    private Integer companyId;

    @Column(nullable = false, length = 100)
    private String name;
}