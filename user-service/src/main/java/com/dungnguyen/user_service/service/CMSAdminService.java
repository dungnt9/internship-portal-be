package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.dto.cms.CMSAdminCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSAdminDTO;
import com.dungnguyen.user_service.dto.cms.CMSAdminUpdateDTO;
import com.dungnguyen.user_service.entity.Admin;
import com.dungnguyen.user_service.exception.AdminNotFoundException;
import com.dungnguyen.user_service.repository.AdminRepository;
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
public class CMSAdminService {

    private final AdminRepository adminRepository;
    private final CMSAuthServiceClient authServiceClient;

    public List<CMSAdminDTO> getAllAdmins() {
        // Get all admins from the repository
        List<Admin> admins = adminRepository.findAll();

        // Get all admin users from auth service
        List<UserResponseDTO> authUsers = authServiceClient.getAllUsers();

        // Filter to keep only admin users
        if (authUsers != null) {
            authUsers = authUsers.stream()
                    .filter(user -> "ROLE_ADMIN".equals(user.getRole()))
                    .collect(Collectors.toList());

            // Create a map of auth users for easy lookup
            Map<Integer, UserResponseDTO> authUserMap = authUsers.stream()
                    .collect(Collectors.toMap(UserResponseDTO::getId, Function.identity()));

            // Combine the data from both sources
            List<CMSAdminDTO> result = new ArrayList<>();

            for (Admin admin : admins) {
                CMSAdminDTO dto = new CMSAdminDTO(admin);
                UserResponseDTO authUser = authUserMap.get(admin.getAuthUserId());

                if (authUser != null) {
                    dto.setEmail(authUser.getEmail());
                    dto.setPhone(authUser.getPhone());
                    dto.setIsActive(authUser.getIsActive());

                    result.add(dto);

                    // Remove this auth user from the map as it's been processed
                    authUserMap.remove(admin.getAuthUserId());
                }
            }

            // Add any remaining admin auth users that don't have corresponding admin records
            for (UserResponseDTO remainingAuthUser : authUserMap.values()) {
                if ("ROLE_ADMIN".equals(remainingAuthUser.getRole())) {
                    CMSAdminDTO dto = new CMSAdminDTO();
                    dto.setAuthUserId(remainingAuthUser.getId());
                    dto.setEmail(remainingAuthUser.getEmail());
                    dto.setPhone(remainingAuthUser.getPhone());
                    dto.setIsActive(remainingAuthUser.getIsActive());

                    result.add(dto);
                }
            }

            return result;
        }

        // If we couldn't get auth users, just return admin data without auth info
        return admins.stream().map(CMSAdminDTO::new).collect(Collectors.toList());
    }

    public CMSAdminDTO getAdminById(Integer id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + id));

        CMSAdminDTO adminDTO = new CMSAdminDTO(admin);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(admin.getAuthUserId());
        if (userResponse != null) {
            adminDTO.setEmail(userResponse.getEmail());
            adminDTO.setPhone(userResponse.getPhone());
            adminDTO.setIsActive(userResponse.getIsActive());
        }

        return adminDTO;
    }

    @Transactional
    public CMSAdminDTO createAdmin(CMSAdminCreateDTO createDTO) {
        // First, create the user in auth service
        CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
        authUserDTO.setEmail(createDTO.getEmail());
        authUserDTO.setPhone(createDTO.getPhone());
        authUserDTO.setPassword(createDTO.getPassword());
        authUserDTO.setRoleName("ROLE_ADMIN");
        authUserDTO.setIsActive(true);

        UserResponseDTO createdAuthUser = authServiceClient.createUser(authUserDTO);

        if (createdAuthUser == null) {
            throw new RuntimeException("Failed to create user in Auth Service");
        }

        // Then create the admin in user service
        Admin admin = new Admin();
        admin.setAuthUserId(createdAuthUser.getId());
        admin.setName(createDTO.getName());
        admin.setDepartment(createDTO.getDepartment());
        admin.setPosition(createDTO.getPosition());

        Admin savedAdmin = adminRepository.save(admin);

        // Return combined data
        CMSAdminDTO result = new CMSAdminDTO(savedAdmin);
        result.setEmail(createdAuthUser.getEmail());
        result.setPhone(createdAuthUser.getPhone());
        result.setIsActive(createdAuthUser.getIsActive());

        return result;
    }

    @Transactional
    public CMSAdminDTO updateAdmin(Integer id, CMSAdminUpdateDTO updateDTO) {
        // Find the admin
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + id));

        // Update auth user information if available
        if (updateDTO.getEmail() != null || updateDTO.getPhone() != null ||
                updateDTO.getPassword() != null || updateDTO.getIsActive() != null) {

            CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
            authUserDTO.setEmail(updateDTO.getEmail());
            authUserDTO.setPhone(updateDTO.getPhone());
            authUserDTO.setPassword(updateDTO.getPassword());
            authUserDTO.setIsActive(updateDTO.getIsActive());

            UserResponseDTO updatedAuthUser = authServiceClient.updateUser(admin.getAuthUserId(), authUserDTO);

            if (updatedAuthUser == null) {
                throw new RuntimeException("Failed to update user in Auth Service");
            }
        }

        // Update admin information
        if (updateDTO.getName() != null) {
            admin.setName(updateDTO.getName());
        }
        if (updateDTO.getDepartment() != null) {
            admin.setDepartment(updateDTO.getDepartment());
        }
        if (updateDTO.getPosition() != null) {
            admin.setPosition(updateDTO.getPosition());
        }

        Admin updatedAdmin = adminRepository.save(admin);

        // Return combined data
        CMSAdminDTO result = new CMSAdminDTO(updatedAdmin);

        // Fetch updated user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(admin.getAuthUserId());
        if (userResponse != null) {
            result.setEmail(userResponse.getEmail());
            result.setPhone(userResponse.getPhone());
            result.setIsActive(userResponse.getIsActive());
        }

        return result;
    }
}