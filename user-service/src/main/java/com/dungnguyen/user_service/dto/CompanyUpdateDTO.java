package com.dungnguyen.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateDTO {
    private String name;
    private String shortName;
    private Boolean isForeignCompany;
    private String taxCode;
    private String website;
    private String address;
    private String businessType;
    private String description;
    private Integer foundingYear;
    private Integer employeeCount;
    private BigDecimal capital;
    // Note: logoPath is typically handled separately through file upload endpoints
    // Note: The remaining fields (isVerified, verificationDate, rejectionReason, isLinked)
    //       are typically handled by admin operations only
}