package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMSExternalInternshipUpdateDTO {
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED
}