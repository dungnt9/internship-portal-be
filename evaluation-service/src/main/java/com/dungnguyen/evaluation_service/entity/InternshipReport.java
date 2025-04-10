package com.dungnguyen.evaluation_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "internship_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "progress_ref_id", nullable = false)
    private ProgressReference progressRef;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;
}