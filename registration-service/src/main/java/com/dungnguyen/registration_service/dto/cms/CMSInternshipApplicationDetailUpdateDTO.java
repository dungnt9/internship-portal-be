package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipApplicationDetailUpdateDTO {
    private Integer preferenceOrder;
    private String status;
    private String note;
}