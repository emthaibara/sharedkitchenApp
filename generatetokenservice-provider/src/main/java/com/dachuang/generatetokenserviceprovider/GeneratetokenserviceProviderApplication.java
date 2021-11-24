package com.dachuang.generatetokenserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GeneratetokenserviceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratetokenserviceProviderApplication.class, args);
    }

}
