package com.dachuang.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */
@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator gateway(RouteLocatorBuilder rlb){
        return rlb
                .routes()
                .route(p -> p
                        .path("/sharedkitchen/sign")
                        .uri("lb://signservice-provider"))
                .route(p -> p
                        .path("/sharedkitchen/login")
                        .uri("lb://loginservice-provider"))
                .build();
    }

    @Bean
    public StringRedisTemplate redisTemplate(LettuceConnectionFactory connectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

}
