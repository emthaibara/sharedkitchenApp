package com.dachuang.gateway.filter;

import com.dachuang.gateway.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 *
 *      gateway-filter:
 *              首先过滤调请求中无token的request
 *                      快速失败
 */

@Component
public class TokenFilter implements GatewayFilter,Ordered {

    private static final String TOKENNAME = "token";

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        if (Objects.isNull(serverHttpRequest.getHeaders().getFirst(TOKENNAME))){
            return serverHttpResponse.writeWith(Mono.create(monoSink -> {
                try {
                    byte[] bytes = objectMapper.writeValueAsBytes(Result.tokenExpired());
                    DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(bytes);
                    monoSink.success(dataBuffer);
                } catch (JsonProcessingException jsonProcessingException) {
                    monoSink.error(jsonProcessingException);
                }
            }));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
