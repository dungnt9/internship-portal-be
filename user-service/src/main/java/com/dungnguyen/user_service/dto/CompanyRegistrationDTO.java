package com.dungnguyen.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationDTO {
    // Company information
    private String companyName;
    private String shortName;
    private String website;
    private String taxCode;
    private String address;
    private Boolean isForeignCompany;

    // Company contact information
    private String fullName;
    private String email;
    private String phone;
    private String position;
}