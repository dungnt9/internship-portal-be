package com.dungnguyen.user_service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {
    private String email;
    private String phone;
    private String password;
    private String roleName;
    private Boolean isActive;
}