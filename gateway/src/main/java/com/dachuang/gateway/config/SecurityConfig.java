package com.dachuang.gateway.config;

import com.dachuang.gateway.mamager.AuthorizeConfigManager;
import com.dachuang.gateway.mamager.JwtAuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 */

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Resource
    private JwtSecurityContextRepository securityContextRepository;

    @Resource
    @Lazy
    private AuthorizeConfigManager authorizeConfigManager;

    @Resource
    private JwtAuthenticationManager jwtAuthenticationManager;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.securityContextRepository(securityContextRepository)
                .authorizeExchange(exchange -> exchange
                .pathMatchers("/NONMEMBER/*").hasAnyRole("NONMEMBER","MOMBER")
                        .pathMatchers("/MEMBER/*").hasAnyRole("MOMBER","MERCHANT")
                        .pathMatchers("/sharedkitchen/sign").permitAll()
                        .pathMatchers("/sharedkitchen/login").permitAll()
                .anyExchange().access(authorizeConfigManager)
                )
                .logout().disable()
                .formLogin().disable()
                .cors().disable()
                .csrf().disable()
        .build();
    }

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        List<ReactiveAuthenticationManager> managers = new ArrayList<>();
        managers.add(jwtAuthenticationManager);
        return new DelegatingReactiveAuthenticationManager(managers);
    }


}
