package com.dungnguyen.auth_service.service;

import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // If the identifier is a numeric value, it's a user ID
        try {
            Integer userId = Integer.parseInt(identifier);
            User user = findById(userId);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with ID: " + identifier);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getId().toString(),
                    user.getPassword(),
                    user.getIsActive(),
                    true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()))
            );
        } catch (NumberFormatException e) {
            // If it's not a numeric value, find user by email or phone
            User user = findByIdentifier(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

            return new org.springframework.security.core.userdetails.User(
                    user.getId().toString(), // Use ID instead of username
                    user.getPassword(),
                    user.getIsActive(),
                    true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()))
            );
        }
    }

    /**
     * Find a user by email or phone
     * @param identifier can be email or phone
     * @return Optional<User>
     */
    public Optional<User> findByIdentifier(String identifier) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user.isPresent()) {
            return user;
        }

        return userRepository.findByPhone(identifier);
    }

    /**
     * Find a user by ID
     * @param id User ID
     * @return User or null if not found
     */
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Find a user by email
     * @param email User email
     * @return User or null if not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Find a user by phone
     * @param phone User phone
     * @return User or null if not found
     */
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
}