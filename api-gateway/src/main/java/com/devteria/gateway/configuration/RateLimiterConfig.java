package com.devteria.gateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {


    @Bean
    @Primary
    public RedisRateLimiter myRateLimiter() {
        return new RedisRateLimiter(20, 50);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            System.out.println(">>> Checking Rate Limit for user: TEST_USER");
            return Mono.just("TEST_USER");
        };
    }
}