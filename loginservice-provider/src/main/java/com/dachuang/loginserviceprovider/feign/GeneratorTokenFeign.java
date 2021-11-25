package com.dachuang.loginserviceprovider.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("generator-token-service-provider")
public interface GeneratorTokenFeign {

    @RequestMapping(method = RequestMethod.GET,value = "/generatortoken")
    String generatorToken(
            @RequestParam(value = "phoneNumber",required = false) String phoneNumber,
            @RequestParam(value = "role",required = false) String role);

}
