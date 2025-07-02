package com.dungnguyen.auth_service.config;

import com.dungnguyen.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;//Lombok tự động sinh constructor có final fields
import org.springframework.context.annotation.Bean;//Đánh dấu method trả về 1 bean để Spring quản lý
import org.springframework.context.annotation.Configuration;//Đánh dấu class là nơi định nghĩa các bean cấu hình
import org.springframework.security.authentication.AuthenticationManager;//Thành phần trung tâm để xử lý xác thực (gọi AuthenticationProvider)
import org.springframework.security.authentication.AuthenticationProvider;//Cung cấp logic xác thực
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;//Provider mặc định của Spring Security, dùng UserDetailsService + PasswordEncoder.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; //Cho phép lấy AuthenticationManager được config sẵn từ Spring context.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;//Cấu hình HTTP Security, định nghĩa các rule bảo mật (cho phép, cấm, filter,...)
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;//Bật cấu hình bảo mật web trong Spring Security
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //Định nghĩa các rule ai được truy cập URL nào
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Cho phép tất cả các request không cần xác thực
                )
                //Cấu hình Không tạo session – dùng cho API kiểu token
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        //Xây dựng SecurityFilterChain
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Provider dùng DB để xác thực
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Lấy manager do Spring tạo sẵn
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}