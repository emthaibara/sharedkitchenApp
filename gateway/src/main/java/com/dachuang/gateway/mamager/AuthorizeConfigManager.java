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
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.file.AccessDeniedException;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 */
@Component
public class AuthorizeConfigManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeConfigManager.class);

    @Resource
    private AntPathMatcher antPathMatcher;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {

        return authentication.map(auth -> {

            ServerHttpRequest httpRequest = object.getExchange().getRequest();

            //遍历权限----当然我这里就一个权限{MOMBEN,NONMEMBER,MERCHANT}
            for (GrantedAuthority authority : auth.getAuthorities()) {
                String authorityAuthority = authority.getAuthority();
                String path = httpRequest.getURI().getPath();
                log.info("authorityAuthority:"+authorityAuthority+"       path:"+path);
                //
                if (antPathMatcher.match(authorityAuthority, path)) {
                    log.info(String.format("用户请求API校验通过，GrantedAuthority:{%s}  Path:{%s} ", authorityAuthority, path));
                    //授权通过
                    log.info("AuthorizationDecision true");
                    return new AuthorizationDecision(true);
                }
                log.info("AuthorizationDecision false");
            }
            //授权失败
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return check(authentication, object)
                //返回一个Mono<AuthorizationDecision>类型
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> {
                    Result<?> result = new Result<>().error("当前用户没有访问权限! ", null);
                    return Mono.error(new AccessDeniedException(Result.resultToJsonString(result)));
                }))
                .flatMap(d -> Mono.empty());
    }
}
