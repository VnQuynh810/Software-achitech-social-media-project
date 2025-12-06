package com.devteria.gateway.filter;

import com.devteria.gateway.client.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthCheckFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // --- SỬA ĐOẠN NÀY ---
        // Vì filter chạy SAU RewritePath, path lúc này đã mất "/v1" (thành /api/auth/login)
        // Nên ta dùng .contains() hoặc check cả 2 trường hợp cho chắc ăn.
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);
        }
        // --------------------

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Gọi Feign Client kiểm tra token
        // Lưu ý: Feign này chạy được nhờ Bean HttpMessageConverters ta vừa thêm
        boolean valid = Boolean.TRUE.equals(authServiceClient.introspect(authHeader));
        if (!valid) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Giữ nguyên Order 3 để chạy SAU Rate Limiter (Order 2)
        return 3;
    }
}