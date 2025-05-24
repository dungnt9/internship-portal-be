package com.dungnguyen.registration_service.dto.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMSInternshipApplicationDTO {
    private Integer id;
    private Integer studentId;
    private String studentCode;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String periodId;
    private String cvFilePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CMSInternshipApplicationDetailDTO> details;
}
