package com.dungnguyen.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContactUpdateDTO {
    private String name;
    private String position;
    // Note: Company contact cannot change their company_id through update endpoint
}