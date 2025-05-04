package com.dungnguyen.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;
}