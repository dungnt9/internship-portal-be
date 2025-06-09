package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyContactCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyContactDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyContactUpdateDTO;
import com.dungnguyen.user_service.entity.Company;
import com.dungnguyen.user_service.entity.CompanyContact;
import com.dungnguyen.user_service.exception.CompanyContactNotFoundException;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.repository.CMSCompanyRepository;
import com.dungnguyen.user_service.repository.CompanyContactRepository;
import com.dungnguyen.user_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSCompanyContactService {

    private final CompanyContactRepository companyContactRepository;
    private final CompanyRepository companyRepository;
    private final CMSAuthServiceClient authServiceClient;
    private final CMSCompanyRepository cmsCompanyRepository;

    public List<CMSCompanyContactDTO> getAllCompanyContacts() {
        // Get all company contacts from the repository
        List<CompanyContact> contacts = companyContactRepository.findAll();

        // Get all company contact users from auth service
        List<UserResponseDTO> authUsers = authServiceClient.getAllUsers();

        // Filter to keep only company contact users
        if (authUsers != null) {
            authUsers = authUsers.stream()
                    .filter(user -> "ROLE_COMPANY".equals(user.getRole()))
                    .collect(Collectors.toList());

            // Create a map of auth users for easy lookup
            Map<Integer, UserResponseDTO> authUserMap = authUsers.stream()
                    .collect(Collectors.toMap(UserResponseDTO::getId, Function.identity()));

            // Combine the data from both sources
            List<CMSCompanyContactDTO> result = new ArrayList<>();

            for (CompanyContact contact : contacts) {
                CMSCompanyContactDTO dto = new CMSCompanyContactDTO(contact);
                UserResponseDTO authUser = authUserMap.get(contact.getAuthUserId());

                if (authUser != null) {
                    dto.setEmail(authUser.getEmail());
                    dto.setPhone(authUser.getPhone());
                    dto.setIsActive(authUser.getIsActive());

                    result.add(dto);

                    // Remove this auth user from the map as it's been processed
                    authUserMap.remove(contact.getAuthUserId());
                }
            }

            // Add any remaining company contact auth users that don't have corresponding contact records
            for (UserResponseDTO remainingAuthUser : authUserMap.values()) {
                if ("ROLE_COMPANY".equals(remainingAuthUser.getRole())) {
                    CMSCompanyContactDTO dto = new CMSCompanyContactDTO();
                    dto.setAuthUserId(remainingAuthUser.getId());
                    dto.setEmail(remainingAuthUser.getEmail());
                    dto.setPhone(remainingAuthUser.getPhone());
                    dto.setIsActive(remainingAuthUser.getIsActive());

                    result.add(dto);
                }
            }

            return result;
        }

        // If we couldn't get auth users, just return company contact data without auth info
        return contacts.stream().map(CMSCompanyContactDTO::new).collect(Collectors.toList());
    }

    public CMSCompanyContactDTO getCompanyContactById(Integer id) {
        CompanyContact contact = companyContactRepository.findById(id)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with ID: " + id));

        CMSCompanyContactDTO contactDTO = new CMSCompanyContactDTO(contact);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(contact.getAuthUserId());
        if (userResponse != null) {
            contactDTO.setEmail(userResponse.getEmail());
            contactDTO.setPhone(userResponse.getPhone());
            contactDTO.setIsActive(userResponse.getIsActive());
        }

        return contactDTO;
    }

    @Transactional
    public CMSCompanyContactDTO createCompanyContact(CMSCompanyContactCreateDTO createDTO) {
        // Validate company exists
        Company company = cmsCompanyRepository.findById(createDTO.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + createDTO.getCompanyId()));

        // First, create the user in auth service
        CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
        authUserDTO.setEmail(createDTO.getEmail());
        authUserDTO.setPhone(createDTO.getPhone());
        authUserDTO.setPassword(createDTO.getPassword());
        authUserDTO.setRoleName("ROLE_COMPANY");
        authUserDTO.setIsActive(true);

        UserResponseDTO createdAuthUser = authServiceClient.createUser(authUserDTO);

        if (createdAuthUser == null) {
            throw new RuntimeException("Failed to create user in Auth Service");
        }

        // Then create the company contact in user service
        CompanyContact contact = new CompanyContact();
        contact.setAuthUserId(createdAuthUser.getId());
        contact.setCompany(company);
        contact.setName(createDTO.getName());
        contact.setPosition(createDTO.getPosition());

        CompanyContact savedContact = companyContactRepository.save(contact);

        // Return combined data
        CMSCompanyContactDTO result = new CMSCompanyContactDTO(savedContact);
        result.setEmail(createdAuthUser.getEmail());
        result.setPhone(createdAuthUser.getPhone());
        result.setIsActive(createdAuthUser.getIsActive());

        return result;
    }

    @Transactional
    public CMSCompanyContactDTO updateCompanyContact(Integer id, CMSCompanyContactUpdateDTO updateDTO) {
        // Find the company contact
        CompanyContact contact = companyContactRepository.findById(id)
                .orElseThrow(() -> new CompanyContactNotFoundException("Company contact not found with ID: " + id));

        // Update auth user information if available
        if (updateDTO.getEmail() != null || updateDTO.getPhone() != null ||
                updateDTO.getPassword() != null || updateDTO.getIsActive() != null) {

            CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
            authUserDTO.setEmail(updateDTO.getEmail());
            authUserDTO.setPhone(updateDTO.getPhone());
            authUserDTO.setPassword(updateDTO.getPassword());
            authUserDTO.setIsActive(updateDTO.getIsActive());

            UserResponseDTO updatedAuthUser = authServiceClient.updateUser(contact.getAuthUserId(), authUserDTO);

            if (updatedAuthUser == null) {
                throw new RuntimeException("Failed to update user in Auth Service");
            }
        }

        // Update company if provided
        if (updateDTO.getCompanyId() != null && !updateDTO.getCompanyId().equals(contact.getCompany().getId())) {
            Company newCompany = cmsCompanyRepository.findById(updateDTO.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + updateDTO.getCompanyId()));
            contact.setCompany(newCompany);
        }

        // Update contact information
        if (updateDTO.getName() != null) {
            contact.setName(updateDTO.getName());
        }
        if (updateDTO.getPosition() != null) {
            contact.setPosition(updateDTO.getPosition());
        }

        CompanyContact updatedContact = companyContactRepository.save(contact);

        // Return combined data
        CMSCompanyContactDTO result = new CMSCompanyContactDTO(updatedContact);

        // Fetch updated user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(contact.getAuthUserId());
        if (userResponse != null) {
            result.setEmail(userResponse.getEmail());
            result.setPhone(userResponse.getPhone());
            result.setIsActive(userResponse.getIsActive());
        }

        return result;
    }
}