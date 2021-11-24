package com.dachuang.jwtverifyandauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class JwtVerifyandauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtVerifyandauthApplication.class, args);
    }
}
