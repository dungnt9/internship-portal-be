package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for confirming an internship progress by a teacher
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipProgressConfirmDTO {
    private Boolean confirm = true;
}