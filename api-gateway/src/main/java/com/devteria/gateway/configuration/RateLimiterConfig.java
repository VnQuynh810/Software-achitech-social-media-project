package com.devteria.gateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    // 1. Tạo Bean Rate Limiter cấu hình cứng ở đây
    // Tham số 1: replenishRate (tốc độ hồi - 1 request/s)
    // Tham số 2: burstCapacity (dung lượng tối đa - 1 request)
    @Bean
    @Primary
    public RedisRateLimiter myRateLimiter() {
        return new RedisRateLimiter(2, 10);
    }

    // 2. Key Resolver (Sửa tạm thành key cứng để test cho dễ)
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            System.out.println(">>> Checking Rate Limit for user: TEST_USER");
            return Mono.just("TEST_USER");
        };
    }
}