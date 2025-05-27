package com.dungnguyen.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyContactDTO {
    private String name;
    private String position;
    private String email;
    private String phone;
}
