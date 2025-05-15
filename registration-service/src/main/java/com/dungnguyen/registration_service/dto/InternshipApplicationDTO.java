package com.dungnguyen.registration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipApplicationDTO {
    private Integer id;
    private Integer studentId;
    private String periodId;
    private String cvFilePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InternshipApplicationDetailDTO> details;
}