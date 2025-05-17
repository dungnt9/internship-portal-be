package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Integer id;
    private Integer authUserId;
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
    private String imagePath;
}