package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.CompanyRegistrationDTO;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.Company;
import com.dungnguyen.user_service.entity.CompanyContact;
import com.dungnguyen.user_service.repository.CompanyContactRepository;
import com.dungnguyen.user_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyRegistrationService {

    private final CompanyRepository companyRepository;
    private final CompanyContactRepository companyContactRepository;
    private final CMSAuthServiceClient authServiceClient;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:dung9102003hp@gmail.com}")
    private String mailFrom;

    @Transactional
    public void registerCompany(CompanyRegistrationDTO registrationDTO) {
        // Validate company uniqueness
        validateCompanyUniqueness(registrationDTO);

        // Validate contact uniqueness
        validateContactUniqueness(registrationDTO);

        // Generate random 6-digit password
        String randomPassword = generateRandomPassword();

        try {
            // 1. Create user in auth service
            CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
            authUserDTO.setEmail(registrationDTO.getEmail());
            authUserDTO.setPhone(registrationDTO.getPhone());
            authUserDTO.setPassword(randomPassword);
            authUserDTO.setRoleName("ROLE_COMPANY");
            authUserDTO.setIsActive(true);

            log.info("Creating auth user for company contact: {}", registrationDTO.getEmail());
            UserResponseDTO createdAuthUser = authServiceClient.createUser(authUserDTO);

            if (createdAuthUser == null) {
                throw new RuntimeException("Failed to create user in auth service");
            }

            // 2. Create company
            Company company = new Company();
            company.setName(registrationDTO.getCompanyName());
            company.setShortName(registrationDTO.getShortName());
            company.setWebsite(registrationDTO.getWebsite());
            company.setTaxCode(registrationDTO.getTaxCode());
            company.setAddress(registrationDTO.getAddress());
            company.setIsForeignCompany(registrationDTO.getIsForeignCompany() != null ?
                    registrationDTO.getIsForeignCompany() : false);
            company.setIsVerified(false);
            company.setIsLinked(false);

            Company savedCompany = companyRepository.save(company);
            log.info("Created company with ID: {}", savedCompany.getId());

            // 3. Create company contact
            CompanyContact companyContact = new CompanyContact();
            companyContact.setAuthUserId(createdAuthUser.getId());
            companyContact.setCompany(savedCompany);
            companyContact.setName(registrationDTO.getFullName());
            companyContact.setPosition(registrationDTO.getPosition());
            companyContact.setImagePath("/images/avatars/default-company-contact.png");

            CompanyContact savedContact = companyContactRepository.save(companyContact);
            log.info("Created company contact with ID: {}", savedContact.getId());

            // 4. Send email with password
            sendRegistrationEmail(registrationDTO.getEmail(), registrationDTO.getFullName(), randomPassword);

        } catch (Exception e) {
            log.error("Error during company registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    private void validateCompanyUniqueness(CompanyRegistrationDTO dto) {
        // For foreign companies, tax code check is not required
        if (dto.getIsForeignCompany() == null || !dto.getIsForeignCompany()) {
            if (dto.getTaxCode() == null || dto.getTaxCode().trim().isEmpty()) {
                throw new IllegalArgumentException("Tax code is required for domestic companies");
            }

            // Check if tax code already exists with verified company
            List<Company> existingCompanies = companyRepository.findAll().stream()
                    .filter(c -> dto.getTaxCode().equals(c.getTaxCode()))
                    .filter(c -> Boolean.TRUE.equals(c.getIsVerified()))
                    .toList();

            if (!existingCompanies.isEmpty()) {
                throw new IllegalArgumentException("Company with this tax code already exists and is verified");
            }
        }
    }

    private void validateContactUniqueness(CompanyRegistrationDTO dto) {
        // Check if email already exists in auth service
        List<UserResponseDTO> allUsers = authServiceClient.getAllUsers();
        if (allUsers != null) {
            boolean emailExists = allUsers.stream()
                    .anyMatch(user -> dto.getEmail().equalsIgnoreCase(user.getEmail()));

            if (emailExists) {
                throw new IllegalArgumentException("Email already exists");
            }

            // Check if phone already exists
            boolean phoneExists = allUsers.stream()
                    .anyMatch(user -> dto.getPhone().equals(user.getPhone()));

            if (phoneExists) {
                throw new IllegalArgumentException("Phone number already exists");
            }
        }
    }

    private String generateRandomPassword() {
        Random random = new Random();
        int password = 10000000 + random.nextInt(90000000); // 8-digit number
        return String.valueOf(password);
    }

    private void sendRegistrationEmail(String email, String fullName, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject("Đăng ký tài khoản doanh nghiệp thành công");
            message.setText("Kính gửi " + fullName + ",\n\n" +
                    "Tài khoản doanh nghiệp của bạn đã được đăng ký thành công.\n\n" +
                    "Thông tin đăng nhập:\n" +
                    "Email: " + email + "\n" +
                    "Mật khẩu: " + password + "\n\n" +
                    "Vui lòng đăng nhập và đổi mật khẩu ngay sau khi nhận được email này.\n" +
                    "Lưu ý: Tài khoản của bạn cần được quản trị viên phê duyệt trước khi có thể sử dụng đầy đủ các chức năng.\n\n" +
                    "Trân trọng,\n" +
                    "Ban quản trị hệ thống");

            log.info("=========================================");
            log.info("Mật khẩu đăng ký: {}", password);
            log.info("=========================================");

            mailSender.send(message);
            log.info("Registration email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", email, e.getMessage());
            // Don't throw exception here to not break the registration process
        }
    }
}