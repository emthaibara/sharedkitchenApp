package com.dachuang.gateway.config;

import com.dachuang.gateway.mamager.AuthorizeConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
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
    private JwtSecurityContextRepository securityContextRepository;

    @Resource
    private AuthorizeConfigManager authorizeConfigManager;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.securityContextRepository(securityContextRepository).build();
    }


    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }


    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
