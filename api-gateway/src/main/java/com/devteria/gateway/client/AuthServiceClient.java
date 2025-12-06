package com.devteria.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "http://localhost:8081"   // sau này có service discovery thì chỉ cần name
)
public interface AuthServiceClient {

    @PostMapping("/api/auth/introspect")
    Boolean introspect(@RequestHeader("Authorization") String token);
}
