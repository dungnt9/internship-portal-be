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

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeRoles();
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
                    createUser("admin01@hust.edu.vn", encodedPassword, "0987654321", adminRole),
                    createUser("admin02@hust.edu.vn", encodedPassword, "0987654322", adminRole)
            );
            userRepository.saveAll(adminUsers);

            // Teacher users
            List<User> teacherUsers = List.of(
                    createUser("nam.vt@hust.edu.vn", encodedPassword, "0312345678", teacherRole),
                    createUser("hoang.nt@hust.edu.vn", encodedPassword, "0312345679", teacherRole),
                    createUser("hung.ld@hust.edu.vn", encodedPassword, "0312345680", teacherRole),
                    createUser("cuong.pv@hust.edu.vn", encodedPassword, "0312345681", teacherRole)
            );
            userRepository.saveAll(teacherUsers);

            // Company Contact users
            List<User> companyContactUsers = List.of(
                    createUser("huong.nt@fpt.com.vn", encodedPassword, "0912345678", companyRole),
                    createUser("nam.pv@fpt.com.vn", encodedPassword, "0912345679", companyRole),
                    createUser("hung.tv@viettel.com.vn", encodedPassword, "0912345680", companyRole),
                    createUser("mai.lt@viettel.com.vn", encodedPassword, "0912345681", companyRole),
                    createUser("thang.nv@vnpt.com.vn", encodedPassword, "0912345682", companyRole),
                    createUser("ha.pt@misa.com.vn", encodedPassword, "0912345683", companyRole),
                    createUser("duc.vm@cmcglobal.vn", encodedPassword, "0912345684", companyRole),
                    createUser("toan.nv@microsoft.com", encodedPassword, "0912345685", companyRole),
                    createUser("linh.pt@google.com", encodedPassword, "0912345686", companyRole)
            );
            userRepository.saveAll(companyContactUsers);

            // Student users
            List<User> studentUsers = List.of(
                    createUser("dung.nt216805@sis.hust.edu.vn", encodedPassword, "0912876543", studentRole),
                    createUser("dungntelcom@gmail.com", encodedPassword, "0912876544", studentRole),
                    createUser("trang.nt216456@sis.hust.edu.vn", encodedPassword, "0912876545", studentRole),
                    createUser("minh.vq216789@sis.hust.edu.vn", encodedPassword, "0912876546", studentRole),
                    createUser("son.nh216012@sis.hust.edu.vn", encodedPassword, "0912876547", studentRole),
                    createUser("hien.nv216345@sis.hust.edu.vn", encodedPassword, "0912876548", studentRole),
                    createUser("thanh.nv216678@sis.hust.edu.vn", encodedPassword, "0912876549", studentRole),
                    createUser("lam.nt216901@sis.hust.edu.vn", encodedPassword, "0912876550", studentRole)
            );
            userRepository.saveAll(studentUsers);

            log.info("Users initialized with {} entries",
                    adminUsers.size() + teacherUsers.size() + companyContactUsers.size() + studentUsers.size());
        }
    }

    private User createUser(String email, String password, String phone, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);
        user.setDeletedAt(null);
        return user;
    }
}