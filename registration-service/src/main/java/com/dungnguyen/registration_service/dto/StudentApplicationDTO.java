package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentApplicationDTO {
    // Basic application info
    private Integer applicationId;
    private Integer applicationDetailId;
    private Integer positionId;
    private String positionTitle;
    private Integer preferenceOrder;
    private String status;
    private String note;
    private String cvFilePath;
    private LocalDateTime appliedAt;

    // Student info
    private Integer studentId;
    private String studentCode;
    private String name;
    private String email;
    private String phone;
    private String className;
    private String major;
    private String gender;
    private LocalDate birthday;
    private String address;
    private BigDecimal cpa;
    private String englishLevel;
    private String skills;
}