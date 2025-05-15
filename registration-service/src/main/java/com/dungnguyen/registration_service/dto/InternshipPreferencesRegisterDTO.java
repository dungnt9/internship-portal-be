package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPreferencesRegisterDTO {
    private Integer applicationId;
    private List<InternshipPreferenceDTO> preferences;
}