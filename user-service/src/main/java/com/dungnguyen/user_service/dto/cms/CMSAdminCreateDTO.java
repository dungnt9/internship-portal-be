package com.dungnguyen.user_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSAdminCreateDTO {
    // Auth service information
    private String email;
    private String phone;
    private String password;

    // User service information
    private String name;
    private String department;
    private String position;
}