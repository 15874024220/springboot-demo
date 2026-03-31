package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                    // 放行 WebSocket 端点
                    .requestMatchers("/websocket/**", "/api/websocket/**").permitAll()
                    // 放行测试端点
                    .requestMatchers("/api/test", "/test").permitAll()
                    // 其他所有请求都需要认证
                    .anyRequest().authenticated()
                    // 放行所有请求
                    // .anyRequest().permitAll()
            )
            // 禁用 CSRF（WebSocket 需要）
            .csrf(csrf -> csrf.disable())
            // 禁用表单登录
            .formLogin(form -> form.disable())
            // 禁用 HTTP Basic 认证
            .httpBasic(basic -> basic.disable());
        return http.build();
    }
}