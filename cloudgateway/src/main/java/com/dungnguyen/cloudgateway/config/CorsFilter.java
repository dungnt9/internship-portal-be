package com.dungnguyen.cloudgateway.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Thêm các header CORS nếu chưa có
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "referer,accept,authorization,User-Agent, content-type, xsrf-token,deviceId");
        exchange.getResponse().getHeaders().add("Access-Control-Expose-Headers", "xsrf-token");

        // Nếu là yêu cầu OPTIONS thì trả về mà không xử lý tiếp
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethod().name())) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();  // Không tiếp tục chuỗi xử lý nữa
        }

        return chain.filter(exchange); // Tiếp tục xử lý các yêu cầu không phải OPTIONS
    }
}