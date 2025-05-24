package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipApplicationCreateDTO {
    private Integer studentId;
    private String periodId;
    private String cvFilePath;
}
