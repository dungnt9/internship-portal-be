package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.dto.cms.CMSCompanyContactCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyContactDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyContactUpdateDTO;
import com.dungnguyen.user_service.exception.CompanyContactNotFoundException;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.response.ApiResponse;
import com.dungnguyen.user_service.service.CMSCompanyContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/cms/admin/management/company-contacts")
@RequiredArgsConstructor
@Slf4j
public class CMSCompanyContactController {

    private final CMSCompanyContactService cmsCompanyContactService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CMSCompanyContactDTO>>> getAllCompanyContacts() {
        try {
            List<CMSCompanyContactDTO> contacts = cmsCompanyContactService.getAllCompanyContacts();
            return ResponseEntity.ok(ApiResponse.success(contacts, "Company contacts retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving all company contacts: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CMSCompanyContactDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving company contacts")
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSCompanyContactDTO>> getCompanyContactById(@PathVariable Integer id) {
        try {
            CMSCompanyContactDTO contact = cmsCompanyContactService.getCompanyContactById(id);
            return ResponseEntity.ok(ApiResponse.success(contact, "Company contact retrieved successfully"));
        } catch (CompanyContactNotFoundException e) {
            log.error("Company contact not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving the company contact")
                            .data(null)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CMSCompanyContactDTO>> createCompanyContact(@RequestBody CMSCompanyContactCreateDTO createDTO) {
        try {
            CMSCompanyContactDTO createdContact = cmsCompanyContactService.createCompanyContact(createDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdContact, "Company contact created successfully"));
        } catch (CompanyNotFoundException e) {
            log.error("Company not found: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error creating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error creating company contact: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CMSCompanyContactDTO>> updateCompanyContact(
            @PathVariable Integer id,
            @RequestBody CMSCompanyContactUpdateDTO updateDTO) {
        try {
            CMSCompanyContactDTO updatedContact = cmsCompanyContactService.updateCompanyContact(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedContact, "Company contact updated successfully"));
        } catch (CompanyContactNotFoundException | CompanyNotFoundException e) {
            log.error("Not found error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (HttpClientErrorException e) {
            log.error("Error from Auth Service: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(e.getStatusCode().value())
                            .message("Auth Service error: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating company contact: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<CMSCompanyContactDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Error updating company contact: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}