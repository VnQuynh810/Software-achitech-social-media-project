package com.devteria.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "http://localhost:8081"
)
public interface AuthServiceClient {

    @PostMapping("/api/auth/introspect")
    Boolean introspect(@RequestHeader("Authorization") String token);
}
