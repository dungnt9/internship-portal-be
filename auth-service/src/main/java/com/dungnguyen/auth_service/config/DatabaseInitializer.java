package com.dungnguyen.auth_service.config;

import com.dungnguyen.auth_service.entity.Role;
import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.repository.RoleRepository;
import com.dungnguyen.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Initializes database with sample data on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Initialize roles if they don't exist
        initializeRoles();

        // Initialize users with properly encoded passwords
        initializeUsers();

        log.info("Database initialization completed successfully");
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing roles...");

            List<Role> roles = List.of(
                    new Role(null, "ROLE_ADMIN", "Quản trị viên hệ thống", null, null),
                    new Role(null, "ROLE_TEACHER", "Giảng viên hướng dẫn", null, null),
                    new Role(null, "ROLE_COMPANY", "Doanh nghiệp", null, null),
                    new Role(null, "ROLE_STUDENT", "Sinh viên", null, null)
            );

            roleRepository.saveAll(roles);
            log.info("Roles initialized with {} entries", roles.size());
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing users...");
            String encodedPassword = passwordEncoder.encode("12345678");

            // Get roles
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            Role teacherRole = roleRepository.findByName("ROLE_TEACHER").orElseThrow();
            Role companyRole = roleRepository.findByName("ROLE_COMPANY").orElseThrow();
            Role studentRole = roleRepository.findByName("ROLE_STUDENT").orElseThrow();

            // Admin users
            List<User> adminUsers = List.of(
                    createUser("admin01", "admin01@hust.edu.vn", encodedPassword, "0987654321", adminRole, "/images/avatars/default-admin.png"),
                    createUser("admin02", "admin02@hust.edu.vn", encodedPassword, "0987654322", adminRole, "/images/avatars/default-admin.png")
            );
            userRepository.saveAll(adminUsers);

            // Teacher users
            List<User> teacherUsers = List.of(
                    createUser("nampv", "nam.vt@hust.edu.vn", encodedPassword, "0312345678", teacherRole, "/images/avatars/default-teacher.png"),
                    createUser("hoangnt", "hoang.nt@hust.edu.vn", encodedPassword, "0312345679", teacherRole, "/images/avatars/default-teacher.png"),
                    createUser("hungld", "hung.ld@hust.edu.vn", encodedPassword, "0312345680", teacherRole, "/images/avatars/default-teacher.png"),
                    createUser("cuongpv", "cuong.pv@hust.edu.vn", encodedPassword, "0312345681", teacherRole, "/images/avatars/default-teacher.png")
            );
            userRepository.saveAll(teacherUsers);

            // Company Contact users - all company representatives/contacts use ROLE_COMPANY
            List<User> companyContactUsers = List.of(
                    // FPT Software contacts
                    createUser("huong_fpt", "huong.nt@fpt.com.vn", encodedPassword, "0912345678", companyRole, "/images/avatars/companies/fpt-contact.png"),
                    createUser("nam_fpt", "nam.pv@fpt.com.vn", encodedPassword, "0912345679", companyRole, "/images/avatars/companies/fpt-contact.png"),
                    // Viettel contacts
                    createUser("hung_viettel", "hung.tv@viettel.com.vn", encodedPassword, "0912345680", companyRole, "/images/avatars/companies/viettel-contact.png"),
                    createUser("mai_viettel", "mai.lt@viettel.com.vn", encodedPassword, "0912345681", companyRole, "/images/avatars/companies/viettel-contact.png"),
                    // VNPT contacts
                    createUser("thang_vnpt", "thang.nv@vnpt.com.vn", encodedPassword, "0912345682", companyRole, "/images/avatars/companies/vnpt-contact.png"),
                    // MISA contacts
                    createUser("ha_misa", "ha.pt@misa.com.vn", encodedPassword, "0912345683", companyRole, "/images/avatars/companies/misa-contact.png"),
                    // CMC contacts
                    createUser("duc_cmc", "duc.vm@cmcglobal.vn", encodedPassword, "0912345684", companyRole, "/images/avatars/companies/cmc-contact.png"),
                    // Microsoft contacts
                    createUser("toan_microsoft", "toan.nv@microsoft.com", encodedPassword, "0912345685", companyRole, "/images/avatars/companies/microsoft-contact.png"),
                    // Google contacts
                    createUser("linh_google", "linh.pt@google.com", encodedPassword, "0912345686", companyRole, "/images/avatars/companies/google-contact.png")
            );
            userRepository.saveAll(companyContactUsers);

            // Student users
            List<User> studentUsers = List.of(
                    createUser("dungnt216805", "dung.nt216805@sis.hust.edu.vn", encodedPassword, "0912876543", studentRole, "/images/avatars/default-student.png"),
                    createUser("anhlt216123", "anh.lt216123@sis.hust.edu.vn", encodedPassword, "0912876544", studentRole, "/images/avatars/default-student.png"),
                    createUser("trangnt216456", "trang.nt216456@sis.hust.edu.vn", encodedPassword, "0912876545", studentRole, "/images/avatars/default-student.png"),
                    createUser("minhvq216789", "minh.vq216789@sis.hust.edu.vn", encodedPassword, "0912876546", studentRole, "/images/avatars/default-student.png"),
                    createUser("sonnh216012", "son.nh216012@sis.hust.edu.vn", encodedPassword, "0912876547", studentRole, "/images/avatars/default-student.png"),
                    createUser("hiennv216345", "hien.nv216345@sis.hust.edu.vn", encodedPassword, "0912876548", studentRole, "/images/avatars/default-student.png"),
                    createUser("thanhnv216678", "thanh.nv216678@sis.hust.edu.vn", encodedPassword, "0912876549", studentRole, "/images/avatars/default-student.png"),
                    createUser("lamnt216901", "lam.nt216901@sis.hust.edu.vn", encodedPassword, "0912876550", studentRole, "/images/avatars/default-student.png")
            );
            userRepository.saveAll(studentUsers);

            log.info("Users initialized with {} entries",
                    adminUsers.size() + teacherUsers.size() + companyContactUsers.size() + studentUsers.size());
        }
    }

    private User createUser(String username, String email, String password, String phone, Role role, String imagePath) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);
        user.setImagePath(imagePath);
        return user;
    }
}