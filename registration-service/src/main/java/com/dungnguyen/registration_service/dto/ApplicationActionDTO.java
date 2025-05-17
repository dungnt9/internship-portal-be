package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationActionDTO {
    private Integer applicationDetailId;
    private String action; // "APPROVE" or "REJECT"
}