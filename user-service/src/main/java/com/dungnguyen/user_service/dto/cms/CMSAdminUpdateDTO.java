package com.dungnguyen.user_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSAdminUpdateDTO {
    // Auth service information
    private String email;
    private String phone;
    private String password;
    private Boolean isActive;

    // User service information
    private String name;
    private String department;
    private String position;
}