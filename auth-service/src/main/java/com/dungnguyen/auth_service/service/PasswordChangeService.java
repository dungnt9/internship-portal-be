package com.dungnguyen.auth_service.service;

import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordChangeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Getter
    private String lastErrorMessage = "An error occurred";

    @Transactional
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            lastErrorMessage = "Current password is incorrect";
            log.warn("Current password verification failed for user ID: {}", userId);
            return false;
        }

        // If new password is the same as the current one, return error
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            lastErrorMessage = "New password cannot be the same as your current password";
            log.warn("New password is the same as current password for user ID: {}", userId);
            return false;
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password changed successfully for user ID: {}", userId);
        return true;
    }
}