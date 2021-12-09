package com.dachuang.gateway.config;

import com.dachuang.gateway.filter.JwtFilter;
import com.dachuang.gateway.mamager.JwtAuthenticationManager;
import com.dachuang.gateway.util.JwtUtil;
import com.dachuang.gateway.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.WebFilter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 */

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Resource
    private JwtSecurityContextRepository jwtSecurityContextRepository;

    @Resource
    private JwtAuthenticationManager jwtAuthenticationManager;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http.securityContextRepository(jwtSecurityContextRepository)
                .authenticationManager(jwtAuthenticationManager)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/NONMEMBER/*").hasAnyRole("NONMEMBER","MOMBER")
                        .pathMatchers("/MEMBER/*").hasAnyRole("MOMBER","MERCHANT")
                        .pathMatchers("/MERCHANT/*").hasRole("MERCHANT")
                        .anyExchange().permitAll()
                )
                .logout().disable()
                .formLogin().disable()
                .cors().disable()
                .csrf().disable();
        return http.build();
    }


    @Bean
    public WebFilter webFilter(){
        return new JwtFilter(redisUtil,jwtUtil);
    }

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }
}
