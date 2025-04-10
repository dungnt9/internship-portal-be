package com.dungnguyen.registration_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cv_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "original_cv_id", nullable = false)
    private Integer originalCvId;

    @ManyToOne
    @JoinColumn(name = "student_ref_id", nullable = false)
    private StudentReference studentRef;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
}