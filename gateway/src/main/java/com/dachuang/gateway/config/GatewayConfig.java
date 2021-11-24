package com.dachuang.gateway.config;

import com.dachuang.gateway.filter.TokenFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */

@Configuration
public class GatewayConfig {

    @Resource
    private TokenFilter tokenFilter;

    @Bean
    public RouteLocator gateway(RouteLocatorBuilder rlb){
        return rlb
                .routes()
                .route(p -> p
                        .path("/sharedkitchen/sign")
                        //.filters(f -> f.filter(tokenFilter))
                        .uri("lb://signservice-provider"))
                .build();
    }


}
