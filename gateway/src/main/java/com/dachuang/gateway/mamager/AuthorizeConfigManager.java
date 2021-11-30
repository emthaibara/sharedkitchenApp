package com.dachuang.gateway.mamager;

import com.dachuang.gateway.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 */

public class AuthorizeConfigManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeConfigManager.class);

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        return authentication.map(auth -> {
            ServerWebExchange exchange = object.getExchange();

            ServerHttpRequest request = exchange.getRequest();

            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            //遍历权限----当然我这里就一个权限{MOMBEN,NONMEMBER,MERCHANT}
            for (GrantedAuthority authority : authorities) {
                String authorityAuthority = authority.getAuthority();
                String path = request.getURI().getPath();
                //
                if (antPathMatcher.match(authorityAuthority, path)) {
                    log.info(String.format("用户请求API校验通过，GrantedAuthority:{%s}  Path:{%s} ", authorityAuthority, path));
                    //授权通过
                    return new AuthorizationDecision(true);
                }
            }
            //授权失败
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return check(authentication, object)
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> {
                    Result<?> result = new Result<>().error("当前用户没有访问权限! ", null);
                    return Mono.error(new AccessDeniedException(Result.resultAsJsonString(result)));
                }))
                .flatMap(d -> Mono.empty());
    }

}
