package com.dungnguyen.auth_service.service;

import com.dungnguyen.auth_service.dto.CreateUserRequestDTO;
import com.dungnguyen.auth_service.dto.UserResponseDTO;
import com.dungnguyen.auth_service.entity.Role;
import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.repository.RoleRepository;
import com.dungnguyen.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return convertToDTO(user);
    }

    @Transactional
    public UserResponseDTO createUser(CreateUserRequestDTO requestDTO) {
        // Validate if email already exists
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
        }

        // Validate if phone already exists
        if (requestDTO.getPhone() != null && !requestDTO.getPhone().isEmpty() &&
                userRepository.findByPhone(requestDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists: " + requestDTO.getPhone());
        }

        // Find the role
        Role role = roleRepository.findByName(requestDTO.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + requestDTO.getRoleName()));

        // Create the user
        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        // Set a default password if none provided
        String password = requestDTO.getPassword() != null ? requestDTO.getPassword() : "12345678";
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Integer id, CreateUserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Validate email uniqueness if changed
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
        }

        // Validate phone uniqueness if changed
        if (requestDTO.getPhone() != null && !requestDTO.getPhone().equals(user.getPhone()) &&
                !requestDTO.getPhone().isEmpty() &&
                userRepository.findByPhone(requestDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists: " + requestDTO.getPhone());
        }

        // Update fields if provided
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }

        if (requestDTO.getPhone() != null) {
            user.setPhone(requestDTO.getPhone());
        }

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        if (requestDTO.getRoleName() != null) {
            Role role = roleRepository.findByName(requestDTO.getRoleName())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + requestDTO.getRoleName()));
            user.setRole(role);
        }

        if (requestDTO.getIsActive() != null) {
            user.setIsActive(requestDTO.getIsActive());
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().getName(),
                user.getIsActive()
        );
    }
}