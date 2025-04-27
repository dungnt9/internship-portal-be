package com.dungnguyen.cloudgateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponseDTO {
    private String accessToken;
    private String userId;
    private String email;
    private String phone;
    private String role;  // Added role field
    private Date createdAt;
}