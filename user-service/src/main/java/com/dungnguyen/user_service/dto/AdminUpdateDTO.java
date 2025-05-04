package com.dungnguyen.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateDTO {
    private String name;
    private String department;
    private String position;
}