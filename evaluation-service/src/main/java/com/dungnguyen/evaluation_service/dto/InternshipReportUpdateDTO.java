package com.dungnguyen.evaluation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipReportUpdateDTO {
    private String title;
    private String content;
}