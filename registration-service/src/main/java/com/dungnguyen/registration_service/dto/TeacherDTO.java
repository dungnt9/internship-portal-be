package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Integer id;
    private Integer authUserId;
    private String name;
    private String department;
    private String position;
    private String imagePath;
    private String email;
    private String phone;
}