package com.dachuang.gateway.config;

import com.dachuang.gateway.mamager.JwtAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/6
 */
@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private static final Logger log = LoggerFactory.getLogger(JwtSecurityContextRepository.class);

    public final static String TOKENHEADER = "token";

    @Resource
    private JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(TOKENHEADER);
        log.info(token);

        return jwtAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(token, null)
        ).map(SecurityContextImpl::new);
    }

}
