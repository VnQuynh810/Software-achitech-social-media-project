package com.example.profileservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()     // Bật CORS để K6 Dashboard không lỗi
                .csrf().disable() // Tắt CSRF
                .authorizeRequests(auth -> auth
                        // 1. Cho phép GET dữ liệu user thoải mái (Quan trọng nhất)
                        .antMatchers(HttpMethod.GET, "/users/**").permitAll()
                        // 2. Các request khác (POST/PUT...) vẫn bắt buộc login nếu cần
                        .anyRequest().permitAll() // Hoặc .authenticated() nếu bạn muốn giữ bảo mật các cái khác
                );

        return http.build();
    }

    // Giữ nguyên CORS Filter để tránh lỗi Network Error
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}