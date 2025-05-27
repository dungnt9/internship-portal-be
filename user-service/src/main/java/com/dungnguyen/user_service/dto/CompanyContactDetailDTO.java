package com.dungnguyen.user_service.dto;

import com.dungnguyen.user_service.entity.CompanyContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContactDetailDTO {
    private Integer id;
    private Integer authUserId;
    private Integer companyId;
    private String name;
    private String position;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Company information
    private String companyName;
    private String companyShortName;

    // Auth service information
    private String email;
    private String phone;
    private Boolean isActive;

    // Constructor to convert from entity
    public CompanyContactDetailDTO(CompanyContact companyContact) {
        this.id = companyContact.getId();
        this.authUserId = companyContact.getAuthUserId();
        this.companyId = companyContact.getCompany().getId();
        this.name = companyContact.getName();
        this.position = companyContact.getPosition();
        this.imagePath = companyContact.getImagePath();
        this.createdAt = companyContact.getCreatedAt();
        this.updatedAt = companyContact.getUpdatedAt();

        if (companyContact.getCompany() != null) {
            this.companyName = companyContact.getCompany().getName();
            this.companyShortName = companyContact.getCompany().getShortName();
        }
    }
}