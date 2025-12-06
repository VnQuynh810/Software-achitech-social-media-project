package com.devteria.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters; // 1. Import class này
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableFeignClients
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // 2. THÊM BEAN NÀY ĐỂ FIX LỖI FEIGN TRONG WEBFLUX
    @Bean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters();
    }
}