package com.dachuang.gateway.config;

import com.dachuang.gateway.handler.GlobalExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/8
 */

@Configuration
public class ExceptionConfig {

    @Resource
    private GlobalExceptionHandler globalExceptionHandler;

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler(){
        return globalExceptionHandler;
    }

}
