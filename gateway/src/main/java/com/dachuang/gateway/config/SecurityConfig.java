package com.dachuang.gateway.config;

import com.dachuang.gateway.filter.JwtFilter;
import com.dachuang.gateway.mamager.AuthorizeConfigManager;
import com.dachuang.gateway.mamager.JwtAuthenticationManager;
import com.dachuang.gateway.util.JwtUtil;
import com.dachuang.gateway.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;

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
    @Lazy
    private AuthorizeConfigManager authorizeConfigManager;

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
                .addFilterBefore(new JwtFilter(redisUtil,jwtUtil), SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("*/NONMEMBER/*").hasAnyRole("NONMEMBER","MOMBER")
                        .pathMatchers("*/MEMBER/*").hasAnyRole("MOMBER","MERCHANT")
                        .pathMatchers("*/MERCHANT/*").hasRole("MERCHANT")
                        .pathMatchers("/sharedkitchen/sign").permitAll()
                        .pathMatchers("/sharedkitchen/login").permitAll()
                        .anyExchange().access(authorizeConfigManager)
                )

                .logout().disable()
                .formLogin().disable()
                .cors().disable()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }
}
