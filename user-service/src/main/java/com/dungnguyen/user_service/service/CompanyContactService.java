package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.dto.CompanyContactDTO;
import com.dungnguyen.user_service.dto.CompanyContactUpdateDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.CompanyContact;
import com.dungnguyen.user_service.exception.CompanyContactNotFoundException;
import com.dungnguyen.user_service.repository.CompanyContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyContactService {

    private final CompanyContactRepository companyContactRepository;
    private final AuthServiceClient authServiceClient;
    private final FileStorageService fileStorageService;

    public CompanyContactDTO getCompanyContactByAuthUserId(Integer authUserId, String token) {
        CompanyContact companyContact = companyContactRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with auth user ID: " + authUserId));

        CompanyContactDTO companyContactDTO = new CompanyContactDTO(companyContact);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(authUserId, token);
        if (userResponse != null) {
            companyContactDTO.setEmail(userResponse.getEmail());
            companyContactDTO.setPhone(userResponse.getPhone());
        }

        return companyContactDTO;
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }

    @Transactional
    public CompanyContactDTO updateCompanyContact(Integer authUserId, CompanyContactUpdateDTO updateDTO) {
        CompanyContact companyContact = companyContactRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with auth user ID: " + authUserId));

        // Update company contact information
        if (updateDTO.getName() != null) {
            companyContact.setName(updateDTO.getName());
        }
        if (updateDTO.getPosition() != null) {
            companyContact.setPosition(updateDTO.getPosition());
        }

        CompanyContact updatedCompanyContact = companyContactRepository.save(companyContact);
        return new CompanyContactDTO(updatedCompanyContact);
    }

    @Transactional
    public void updateAvatarPath(Integer authUserId, String imagePath) {
        CompanyContact contact = companyContactRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with auth user ID: " + authUserId));

        // Delete old avatar if exists
        if (contact.getImagePath() != null && !contact.getImagePath().isEmpty()) {
            fileStorageService.deleteFile(contact.getImagePath());
        }

        contact.setImagePath(imagePath);
        companyContactRepository.save(contact);
    }

}