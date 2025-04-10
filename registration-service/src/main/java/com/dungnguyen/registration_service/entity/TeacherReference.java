package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "teacher_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "teacher_id", nullable = false, unique = true)
    private Integer teacherId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
}