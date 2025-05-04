package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.Teacher;
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

    // Auth service information
    private String email;
    private String phone;

    // Constructor to convert from entity
    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.authUserId = teacher.getAuthUserId();
        this.name = teacher.getName();
        this.department = teacher.getDepartment();
        this.position = teacher.getPosition();
        this.imagePath = teacher.getImagePath();
    }
}
