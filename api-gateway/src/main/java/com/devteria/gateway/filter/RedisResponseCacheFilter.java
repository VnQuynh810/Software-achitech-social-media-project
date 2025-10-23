package com.devteria.gateway.filter;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class RedisResponseCacheFilter implements GlobalFilter, Ordered {

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = "cache:" + exchange.getRequest().getURI().toString();
        // Nếu có cache, trả lại luôn
        return redisTemplate.opsForValue().get(key)
                .flatMap(cachedBody -> {
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBuffer buffer = exchange.getResponse().bufferFactory()
                            .wrap(cachedBody.getBytes(StandardCharsets.UTF_8));
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Nếu chưa cache, tiến hành request rồi cache lại response
                    ServerHttpResponseDecorator decoratedResponse = decorateResponse(exchange, key);
                    return chain.filter(exchange.mutate().response(decoratedResponse).build());
                }));
    }

    private ServerHttpResponseDecorator decorateResponse(ServerWebExchange exchange, String key) {
        var originalResponse = exchange.getResponse();
        var bufferFactory = originalResponse.bufferFactory();

        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(
                            fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                String bodyString = new String(content, StandardCharsets.UTF_8);
                                // Lưu JSON body vào Redis
                                redisTemplate.opsForValue().set(key, bodyString, CACHE_TTL).subscribe();
                                // Trả lại response cho client
                                return bufferFactory.wrap(content);
                            })
                    );
                }
                return super.writeWith(body);
            }
        };
    }

    @Override
    public int getOrder() {
        return -2;
    }

    @Bean
    public GlobalFilter jsonPrettyPrinterFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            exchange.getResponse().getHeaders().set("Content-Type", "application/json; charset=UTF-8");
        }));
    }
}
