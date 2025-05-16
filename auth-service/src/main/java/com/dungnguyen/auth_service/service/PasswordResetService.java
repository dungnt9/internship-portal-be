package com.dungnguyen.auth_service.service;

import com.dungnguyen.auth_service.entity.PasswordResetToken;
import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.repository.PasswordResetTokenRepository;
import com.dungnguyen.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${password.reset.token.expiry:15}") // 15 minutes by default
    private int tokenExpiryMinutes;

    @Value("${spring.mail.username:dung9102003hp@gmail.com}")
    private String mailFrom;

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String email, String otp) {
        try {
            // Trong môi trường phát triển, chỉ in OTP ra log
            log.info("======= OTP FOR {} IS: {} =======", email, otp);

//            Tạm thời bỏ qua việc gửi email thực tế
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject("Mã xác nhận đặt lại mật khẩu");
            message.setText("Mã xác nhận của bạn là: " + otp + "\n\n" +
                    "Mã này có hiệu lực trong " + tokenExpiryMinutes + " phút.\n\n" +
                    "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.");

            mailSender.send(message);

            log.info("Giả lập gửi OTP thành công đến: {}", email);
        } catch (Exception e) {
            // Ghi log lỗi nhưng không throw exception để quá trình tiếp tục
            log.error("Failed to send OTP email to {}: {}", email, e.getMessage());
        }
    }

    /**
     * Process forgot password request: Validate email, generate OTP, and send email
     */
    @Transactional
    public void processForgotPassword(String email) {
        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is disabled");
        }

        // Generate OTP
        String otp = generateOtp();

        // Calculate expiry date
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenExpiryMinutes);

        // Save OTP to database
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setExpiryDate(expiryDate);
        token.setUsed(false);

        tokenRepository.save(token);

        try {
            // Gửi OTP đến email người dùng
            sendOtpEmail(email, otp);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            // Không throw exception để frontend vẫn nhận được response thành công
        }
    }

    @Transactional(readOnly = true)
    public boolean verifyOtp(String email, String otp) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByEmailAndOtpAndUsed(email, otp, false);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken token = tokenOpt.get();

        // Check if token is expired
        return !token.isExpired();
    }

    @Transactional
    public boolean resetPassword(String email, String otp, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByEmailAndOtpAndUsed(email, otp, false);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken token = tokenOpt.get();

        // Check if token is expired
        if (token.isExpired()) {
            return false;
        }

        // Find the user
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);

        log.info("Password reset successfully for user: {}", email);
        return true;
    }
}