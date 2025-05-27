package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.*;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.Company;
import com.dungnguyen.user_service.entity.CompanyContact;
import com.dungnguyen.user_service.exception.CompanyContactNotFoundException;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.repository.CompanyContactRepository;
import com.dungnguyen.user_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyContactManagementService {

    private final CompanyContactRepository companyContactRepository;
    private final CompanyRepository companyRepository;
    private final AuthServiceClient authServiceClient;
    private final CMSAuthServiceClient cmsAuthServiceClient;

    /**
     * Get all company contacts of the current user's company
     */
    public List<CompanyContactDetailDTO> getMyCompanyContacts(Integer currentAuthUserId) {
        // First, find the current user's company
        CompanyContact currentContact = companyContactRepository.findByAuthUserId(currentAuthUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        Integer companyId = currentContact.getCompany().getId();

        // Get all contacts of the same company
        List<CompanyContact> companyContacts = companyContactRepository.findByCompanyId(companyId);

        // Convert to DTOs and fetch auth information
        return companyContacts.stream()
                .map(contact -> {
                    CompanyContactDetailDTO dto = new CompanyContactDetailDTO(contact);
                    // Fetch auth information
                    UserResponseDTO userResponse = cmsAuthServiceClient.getUserById(contact.getAuthUserId());
                    if (userResponse != null) {
                        dto.setEmail(userResponse.getEmail());
                        dto.setPhone(userResponse.getPhone());
                        dto.setIsActive(userResponse.getIsActive());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get a specific company contact by ID (within the same company)
     */
    public CompanyContactDetailDTO getCompanyContactById(Integer currentAuthUserId, Integer contactId) {
        // Verify current user is a company contact
        CompanyContact currentContact = companyContactRepository.findByAuthUserId(currentAuthUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        // Find the requested contact
        CompanyContact requestedContact = companyContactRepository.findById(contactId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with ID: " + contactId));

        // Verify they are in the same company
        if (!requestedContact.getCompany().getId().equals(currentContact.getCompany().getId())) {
            throw new IllegalArgumentException("You can only view contacts from your own company");
        }

        CompanyContactDetailDTO dto = new CompanyContactDetailDTO(requestedContact);

        // Fetch auth information
        UserResponseDTO userResponse = cmsAuthServiceClient.getUserById(requestedContact.getAuthUserId());
        if (userResponse != null) {
            dto.setEmail(userResponse.getEmail());
            dto.setPhone(userResponse.getPhone());
            dto.setIsActive(userResponse.getIsActive());
        }

        return dto;
    }

    /**
     * Create a new company contact in the same company
     */
    @Transactional
    public CompanyContactDetailDTO createCompanyContact(Integer currentAuthUserId, CreateCompanyContactDTO createDTO) {
        // Verify current user is a company contact
        CompanyContact currentContact = companyContactRepository.findByAuthUserId(currentAuthUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        try {
            // First, create user in auth service
            CreateUserRequestDTO userRequest = new CreateUserRequestDTO();
            userRequest.setEmail(createDTO.getEmail());
            userRequest.setPhone(createDTO.getPhone());
            userRequest.setPassword(createDTO.getPassword() != null ? createDTO.getPassword() : "12345678");
            userRequest.setRoleName("ROLE_COMPANY");
            userRequest.setIsActive(true);

            log.info("Creating user in auth service: {}", createDTO.getEmail());
            UserResponseDTO createdUser = cmsAuthServiceClient.createUser(userRequest);

            if (createdUser == null) {
                throw new RuntimeException("Failed to create user in auth service");
            }

            // Then create company contact in user service
            CompanyContact newContact = new CompanyContact();
            newContact.setAuthUserId(createdUser.getId());
            newContact.setCompany(currentContact.getCompany()); // Same company as current user
            newContact.setName(createDTO.getName());
            newContact.setPosition(createDTO.getPosition());
            newContact.setImagePath("/images/avatars/default-company-contact.png");

            CompanyContact savedContact = companyContactRepository.save(newContact);
            log.info("Created company contact with ID: {}", savedContact.getId());

            // Convert to DTO and add auth information
            CompanyContactDetailDTO dto = new CompanyContactDetailDTO(savedContact);
            dto.setEmail(createdUser.getEmail());
            dto.setPhone(createdUser.getPhone());
            dto.setIsActive(createdUser.getIsActive());

            return dto;

        } catch (Exception e) {
            log.error("Error creating company contact: {}", e.getMessage());
            throw new RuntimeException("Failed to create company contact: " + e.getMessage());
        }
    }

    /**
     * Update a company contact (within the same company)
     */
    @Transactional
    public CompanyContactDetailDTO updateCompanyContact(Integer currentAuthUserId, Integer contactId, UpdateCompanyContactDTO updateDTO) {
        // Verify current user is a company contact
        CompanyContact currentContact = companyContactRepository.findByAuthUserId(currentAuthUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        // Find the contact to update
        CompanyContact contactToUpdate = companyContactRepository.findById(contactId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with ID: " + contactId));

        // Verify they are in the same company
        if (!contactToUpdate.getCompany().getId().equals(currentContact.getCompany().getId())) {
            throw new IllegalArgumentException("You can only update contacts from your own company");
        }

        try {
            // Update user information in auth service if email or phone changed
            if ((updateDTO.getEmail() != null && !updateDTO.getEmail().isEmpty()) ||
                    (updateDTO.getPhone() != null && !updateDTO.getPhone().isEmpty())) {

                CreateUserRequestDTO userUpdateRequest = new CreateUserRequestDTO();
                userUpdateRequest.setEmail(updateDTO.getEmail());
                userUpdateRequest.setPhone(updateDTO.getPhone());
                userUpdateRequest.setRoleName("ROLE_COMPANY");
                userUpdateRequest.setIsActive(true);

                log.info("Updating user in auth service for contact ID: {}", contactId);
                UserResponseDTO updatedUser = cmsAuthServiceClient.updateUser(contactToUpdate.getAuthUserId(), userUpdateRequest);

                if (updatedUser == null) {
                    log.warn("Failed to update user in auth service, but continuing with contact update");
                }
            }

            // Update company contact information
            if (updateDTO.getName() != null && !updateDTO.getName().isEmpty()) {
                contactToUpdate.setName(updateDTO.getName());
            }
            if (updateDTO.getPosition() != null && !updateDTO.getPosition().isEmpty()) {
                contactToUpdate.setPosition(updateDTO.getPosition());
            }

            CompanyContact updatedContact = companyContactRepository.save(contactToUpdate);
            log.info("Updated company contact with ID: {}", updatedContact.getId());

            // Convert to DTO and fetch current auth information
            CompanyContactDetailDTO dto = new CompanyContactDetailDTO(updatedContact);
            UserResponseDTO userResponse = cmsAuthServiceClient.getUserById(updatedContact.getAuthUserId());
            if (userResponse != null) {
                dto.setEmail(userResponse.getEmail());
                dto.setPhone(userResponse.getPhone());
                dto.setIsActive(userResponse.getIsActive());
            }

            return dto;

        } catch (Exception e) {
            log.error("Error updating company contact: {}", e.getMessage());
            throw new RuntimeException("Failed to update company contact: " + e.getMessage());
        }
    }

    /**
     * Delete a company contact (within the same company)
     * Note: This is a soft delete - we set deletedAt timestamp
     */
    @Transactional
    public void deleteCompanyContact(Integer currentAuthUserId, Integer contactId) {
        // Verify current user is a company contact
        CompanyContact currentContact = companyContactRepository.findByAuthUserId(currentAuthUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        // Find the contact to delete
        CompanyContact contactToDelete = companyContactRepository.findById(contactId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with ID: " + contactId));

        // Verify they are in the same company
        if (!contactToDelete.getCompany().getId().equals(currentContact.getCompany().getId())) {
            throw new IllegalArgumentException("You can only delete contacts from your own company");
        }

        // Prevent self-deletion
        if (contactToDelete.getId().equals(currentContact.getId())) {
            throw new IllegalArgumentException("You cannot delete your own contact");
        }

        try {
            // Soft delete the company contact
            contactToDelete.setDeletedAt(java.time.LocalDateTime.now());
            companyContactRepository.save(contactToDelete);

            log.info("Soft deleted company contact with ID: {}", contactId);

            // Optionally, you might want to deactivate the user in auth service as well
            // This depends on your business requirements

        } catch (Exception e) {
            log.error("Error deleting company contact: {}", e.getMessage());
            throw new RuntimeException("Failed to delete company contact: " + e.getMessage());
        }
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }
}