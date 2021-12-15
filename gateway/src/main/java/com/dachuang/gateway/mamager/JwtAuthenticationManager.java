package com.dachuang.gateway.mamager;


import com.dachuang.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/6
 */
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    private static final String ROLE = "role";

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("username:"+jwtUtil.getDecodedJWT(authentication.getPrincipal().toString()).getSubject()+"role:"+jwtUtil.getDecodedJWT(authentication.getPrincipal().toString()).getClaim(ROLE).asString());
        //装逼写法，感觉可以分开几行写的。。。。

        return Mono.just(authentication)
                .map(auth -> jwtUtil.getDecodedJWT(authentication.getPrincipal().toString()))
                .map(decodedJWT -> new UsernamePasswordAuthenticationToken(
                        decodedJWT.getSubject(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(decodedJWT.getClaim(ROLE).asString())
                        )
                ));
    }
}
