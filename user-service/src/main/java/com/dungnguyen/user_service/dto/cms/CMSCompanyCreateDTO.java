package com.dungnguyen.user_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSCompanyCreateDTO {
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
    private Boolean isVerified;
    private Boolean isLinked;
}