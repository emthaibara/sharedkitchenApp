package com.dachuang.gateway.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dachuang.gateway.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/7
 */
@Order(-1)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof AccessDeniedException ||
                ex instanceof JWTVerificationException ||
                ex instanceof BaseException
                ) {

            log.error(ex.getMessage());
            return response
                    .writeWith(Mono.fromSupplier(() -> {
                        DataBufferFactory bufferFactory = response.bufferFactory();
                            return bufferFactory.wrap(ex.getMessage().getBytes(StandardCharsets.UTF_8));
                    }));
        }

        log.error(ex.getMessage());
        //其他异常暂时不做处理
        return Mono.error(ex);
    }
}
