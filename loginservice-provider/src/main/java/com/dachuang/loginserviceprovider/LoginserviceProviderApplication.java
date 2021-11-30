package com.dachuang.loginserviceprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.dachuang.loginserviceprovider.dao")
public class LoginserviceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginserviceProviderApplication.class, args);
    }

}
