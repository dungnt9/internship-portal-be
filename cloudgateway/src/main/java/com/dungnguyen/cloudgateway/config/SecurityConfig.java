package com.dungnguyen.cloudgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
//    /**
//     * Khi Cần Sử dụng OAuth2 trước khi vào serivce phía trong
//     * @param http
//     * @return
//     */
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange(exchanges -> exchanges
//                        .anyExchange().authenticated())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt());  // Sử dụng OAuth2 Resource Server với JWT token
//        return http.build();
//    }

//    /**
//     * Khi không cần OAuth2 -> việc xác thực sẽ nằm ở trong service dịch vụ
//     * @param http
//     * @return
//     */
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange(exchanges -> exchanges
//                        .anyExchange().authenticated())
//                .httpBasic();  // Sử dụng HTTP Basic Auth hoặc các phương pháp xác thực khác nếu cần
//        return http.build();
//    }


}