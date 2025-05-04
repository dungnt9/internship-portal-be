package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
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
    public AdminDTO(Admin admin) {
        this.id = admin.getId();
        this.authUserId = admin.getAuthUserId();
        this.name = admin.getName();
        this.department = admin.getDepartment();
        this.position = admin.getPosition();
        this.imagePath = admin.getImagePath();
    }
}