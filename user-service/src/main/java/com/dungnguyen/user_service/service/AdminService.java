package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.dto.AdminDTO;
import com.dungnguyen.user_service.dto.AdminUpdateDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.Admin;
import com.dungnguyen.user_service.exception.AdminNotFoundException;
import com.dungnguyen.user_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthServiceClient authServiceClient;
    private final FileStorageService fileStorageService;

    public AdminDTO getAdminByAuthUserId(Integer authUserId, String token) {
        Admin admin = adminRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with auth user ID: " + authUserId));

        AdminDTO adminDTO = new AdminDTO(admin);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(authUserId, token);
        if (userResponse != null) {
            adminDTO.setEmail(userResponse.getEmail());
            adminDTO.setPhone(userResponse.getPhone());
        }

        return adminDTO;
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }

    @Transactional
    public AdminDTO updateAdmin(Integer authUserId, AdminUpdateDTO updateDTO) {
        Admin admin = adminRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with auth user ID: " + authUserId));

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
        return new AdminDTO(updatedAdmin);
    }

    @Transactional
    public void updateAvatarPath(Integer authUserId, String imagePath) {
        Admin admin = adminRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with auth user ID: " + authUserId));

        // Delete old avatar if exists
        if (admin.getImagePath() != null && !admin.getImagePath().isEmpty()) {
            fileStorageService.deleteFile(admin.getImagePath());
        }

        admin.setImagePath(imagePath);
        adminRepository.save(admin);
    }

}