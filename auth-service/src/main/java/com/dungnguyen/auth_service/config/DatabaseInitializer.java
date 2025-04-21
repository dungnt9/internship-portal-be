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

import java.util.List;
import java.util.Optional;

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
                    new Role(1, "ROLE_ADMIN", "Quản trị viên hệ thống", null, null),
                    new Role(2, "ROLE_TEACHER", "Giảng viên hướng dẫn", null, null),
                    new Role(3, "ROLE_COMPANY", "Doanh nghiệp", null, null),
                    new Role(4, "ROLE_STUDENT", "Sinh viên", null, null)
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
                    createUser(1, "admin01", "admin01@hust.edu.vn", encodedPassword, "0987654321", adminRole),
                    createUser(2, "admin02", "admin02@hust.edu.vn", encodedPassword, "0987654322", adminRole)
            );
            userRepository.saveAll(adminUsers);

            // Teacher users
            List<User> teacherUsers = List.of(
                    createUser(11, "nampv", "nam.vt@hust.edu.vn", encodedPassword, "0912345678", teacherRole),
                    createUser(12, "hoangnt", "hoang.nt@hust.edu.vn", encodedPassword, "0912345679", teacherRole),
                    createUser(13, "hungld", "hung.ld@hust.edu.vn", encodedPassword, "0912345680", teacherRole),
                    createUser(14, "cuongpv", "cuong.pv@hust.edu.vn", encodedPassword, "0912345681", teacherRole)
            );
            userRepository.saveAll(teacherUsers);

            // Company users
            List<User> companyUsers = List.of(
                    createUser(21, "fpt_software", "hr@fpt-software.com", encodedPassword, "0983456789", companyRole),
                    createUser(22, "viettel", "hr@viettel.com.vn", encodedPassword, "0983456790", companyRole),
                    createUser(23, "vnpt", "hr@vnpt.vn", encodedPassword, "0983456791", companyRole),
                    createUser(24, "misa", "hr@misa.com.vn", encodedPassword, "0983456792", companyRole),
                    createUser(25, "cmc_global", "hr@cmcglobal.vn", encodedPassword, "0983456793", companyRole)
            );
            userRepository.saveAll(companyUsers);

            // Student users
            List<User> studentUsers = List.of(
                    createUser(101, "dungnt216805", "dung.nt216805@sis.hust.edu.vn", encodedPassword, "0912876543", studentRole),
                    createUser(102, "anhlt216123", "anh.lt216123@sis.hust.edu.vn", encodedPassword, "0912876544", studentRole),
                    createUser(103, "trangnt216456", "trang.nt216456@sis.hust.edu.vn", encodedPassword, "0912876545", studentRole),
                    createUser(104, "minhvq216789", "minh.vq216789@sis.hust.edu.vn", encodedPassword, "0912876546", studentRole),
                    createUser(105, "sonnh216012", "son.nh216012@sis.hust.edu.vn", encodedPassword, "0912876547", studentRole),
                    createUser(106, "hiennv216345", "hien.nv216345@sis.hust.edu.vn", encodedPassword, "0912876548", studentRole),
                    createUser(107, "thanhnv216678", "thanh.nv216678@sis.hust.edu.vn", encodedPassword, "0912876549", studentRole),
                    createUser(108, "lamnt216901", "lam.nt216901@sis.hust.edu.vn", encodedPassword, "0912876550", studentRole)
            );
            userRepository.saveAll(studentUsers);

            // Company Contact users - THÊM MỚI
            List<User> companyContactUsers = List.of(
                    createUser(201, "huong_fpt", "huong.nt@fpt.com.vn", encodedPassword, "0912345678", companyRole),
                    createUser(202, "nam_fpt", "nam.pv@fpt.com.vn", encodedPassword, "0912345679", companyRole),
                    createUser(203, "hung_viettel", "hung.tv@viettel.com.vn", encodedPassword, "0912345680", companyRole),
                    createUser(204, "mai_viettel", "mai.lt@viettel.com.vn", encodedPassword, "0912345681", companyRole),
                    createUser(205, "thang_vnpt", "thang.nv@vnpt.com.vn", encodedPassword, "0912345682", companyRole),
                    createUser(206, "ha_misa", "ha.pt@misa.com.vn", encodedPassword, "0912345683", companyRole),
                    createUser(207, "duc_cmc", "duc.vm@cmcglobal.vn", encodedPassword, "0912345684", companyRole)
            );
            userRepository.saveAll(companyContactUsers);

            log.info("Users initialized with {} entries",
                    adminUsers.size() + teacherUsers.size() + companyUsers.size() + studentUsers.size()
                            + companyContactUsers.size()); // Cập nhật log
        }
    }

    private User createUser(Integer id, String username, String email, String password, String phone, Role role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);
        return user;
    }
}