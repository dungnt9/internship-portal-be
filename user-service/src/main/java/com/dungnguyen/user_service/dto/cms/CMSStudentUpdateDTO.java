package com.dungnguyen.user_service.dto.cms;

import com.dungnguyen.user_service.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSStudentUpdateDTO {
    // Auth service information
    private String email;
    private String phone;
    private String password;
    private Boolean isActive;

    // User service information
    private String studentCode;
    private String name;
    private String className;
    private String major;
    private Student.Gender gender;
    private LocalDate birthday;
    private String address;
    private BigDecimal cpa;
    private String englishLevel;
    private String skills;
}