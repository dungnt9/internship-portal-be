package com.dungnguyen.auth_service.repository;

import com.dungnguyen.auth_service.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    /**
     * Find by email and OTP and not used
     */
    Optional<PasswordResetToken> findByEmailAndOtpAndUsed(String email, String otp, Boolean used);
}