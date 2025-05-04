package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.CompanyContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContactDTO {
    private Integer id;
    private Integer authUserId;
    private Integer companyId;
    private String name;
    private String position;
    private String imagePath;

    // Company information
    private String companyName;
    private String companyShortName;
    private String companyWebsite;

    // Auth service information
    private String email;
    private String phone;

    // Constructor to convert from entity
    public CompanyContactDTO(CompanyContact companyContact) {
        this.id = companyContact.getId();
        this.authUserId = companyContact.getAuthUserId();
        this.companyId = companyContact.getCompany().getId();
        this.name = companyContact.getName();
        this.position = companyContact.getPosition();
        this.imagePath = companyContact.getImagePath();
        if (companyContact.getCompany() != null) {
            this.companyName = companyContact.getCompany().getName();
            this.companyShortName = companyContact.getCompany().getShortName();
            this.companyWebsite = companyContact.getCompany().getWebsite();
        }
    }
}