package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.Student;
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
    private String className;
    private String major;
    private Student.Gender gender;
    private LocalDate birthday;
    private String address;
    private BigDecimal cpa;
    private String englishLevel;
    private String skills;
    private String imagePath;

    // Auth service information
    private String email;
    private String phone;

    // Constructor to convert from entity
    public StudentDTO(Student student) {
        this.id = student.getId();
        this.authUserId = student.getAuthUserId();
        this.studentCode = student.getStudentCode();
        this.name = student.getName();
        this.className = student.getClassName();
        this.major = student.getMajor();
        this.gender = student.getGender();
        this.birthday = student.getBirthday();
        this.address = student.getAddress();
        this.cpa = student.getCpa();
        this.englishLevel = student.getEnglishLevel();
        this.skills = student.getSkills();
        this.imagePath = student.getImagePath();
    }
}