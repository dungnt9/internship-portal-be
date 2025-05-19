package com.dungnguyen.user_service.dto.cms;

import com.dungnguyen.user_service.entity.CompanyContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSCompanyContactDTO {
    private Integer id;
    private Integer authUserId;
    private Integer companyId;
    private String name;
    private String position;
    private String imagePath;

    // Company information
    private String companyName;
    private String companyShortName;

    // Auth service information
    private String email;
    private String phone;
    private Boolean isActive;

    // Constructor to convert from entity
    public CMSCompanyContactDTO(CompanyContact companyContact) {
        this.id = companyContact.getId();
        this.authUserId = companyContact.getAuthUserId();
        if (companyContact.getCompany() != null) {
            this.companyId = companyContact.getCompany().getId();
            this.companyName = companyContact.getCompany().getName();
            this.companyShortName = companyContact.getCompany().getShortName();
        }
        this.name = companyContact.getName();
        this.position = companyContact.getPosition();
        this.imagePath = companyContact.getImagePath();
    }
}