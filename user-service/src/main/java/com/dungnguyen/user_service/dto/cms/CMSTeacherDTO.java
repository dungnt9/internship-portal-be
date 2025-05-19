package com.dungnguyen.user_service.dto.cms;

import com.dungnguyen.user_service.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSTeacherDTO {
    private Integer id;
    private Integer authUserId;
    private String name;
    private String department;
    private String position;
    private String imagePath;

    // Auth service information
    private String email;
    private String phone;
    private Boolean isActive;

    // Constructor to convert from entity
    public CMSTeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.authUserId = teacher.getAuthUserId();
        this.name = teacher.getName();
        this.department = teacher.getDepartment();
        this.position = teacher.getPosition();
        this.imagePath = teacher.getImagePath();
    }
}