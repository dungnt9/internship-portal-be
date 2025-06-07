package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Integer id;
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
    private String logoPath;
    private Boolean isVerified;
    private LocalDateTime verificationDate;
    private Boolean isLinked;

    // Constructor to convert from entity
    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.shortName = company.getShortName();
        this.isForeignCompany = company.getIsForeignCompany();
        this.taxCode = company.getTaxCode();
        this.website = company.getWebsite();
        this.address = company.getAddress();
        this.businessType = company.getBusinessType();
        this.description = company.getDescription();
        this.foundingYear = company.getFoundingYear();
        this.employeeCount = company.getEmployeeCount();
        this.capital = company.getCapital();
        this.logoPath = company.getLogoPath();
        this.isVerified = company.getIsVerified();
        this.verificationDate = company.getVerificationDate();
        this.isLinked = company.getIsLinked();
    }
}