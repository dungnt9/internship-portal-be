package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.cms.CMSCompanyCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyUpdateDTO;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CMSCompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/companies")
@RequiredArgsConstructor
@Slf4j
public class CMSCompanyController {

    private final CMSCompanyService cmsCompanyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSCompanyDTO>>> getAllCompanies() {
        try {
            List<CMSCompanyDTO> companies = cmsCompanyService.getAllCompanies();
            return ResponseEntity.ok(ApiResponse.success(companies, "Companies retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all companies: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSCompanyDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving companies")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSCompanyDTO>> getCompanyById(@PathVariable Integer id) {
        try {
            CMSCompanyDTO company = cmsCompanyService.getCompanyById(id);
            return ResponseEntity.ok(ApiResponse.success(company, "Company retrieved successfully"));
        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSCompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSCompanyDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the company")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSCompanyDTO>> createCompany(@RequestBody CMSCompanyCreateDTO createDTO) {
        try {
            CMSCompanyDTO createdCompany = cmsCompanyService.createCompany(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdCompany, "Company created successfully"));
        } catch (Exception e) {
            log.error("Error creating company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSCompanyDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating company: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSCompanyDTO>> updateCompany(
            @PathVariable Integer id,
            @RequestBody CMSCompanyUpdateDTO updateDTO) {
        try {
            CMSCompanyDTO updatedCompany = cmsCompanyService.updateCompany(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedCompany, "Company updated successfully"));
        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSCompanyDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating company: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSCompanyDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating company: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}