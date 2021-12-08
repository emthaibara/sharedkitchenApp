package com.dachuang.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/7
 */

@RestController
public class TestController {
    /*
      .pathMatchers("/NONMEMBER/*").hasAnyRole("NONMEMBER","MOMBER")
                              .pathMatchers("/MEMBER/*").hasAnyRole("MOMBER","MERCHANT")
                              .pathMatchers("/MERCHANT").hasRole("MERCHANT")
                              .pathMatchers("/sharedkitchen/sign").permitAll()
                              .pathMatchers("/sharedkitchen/login").permitAll()
      */
    @RequestMapping(method = RequestMethod.GET,value = "/NONMEMBER")
    public Mono<String> NONMEMBER(){
        return Mono.just("NONMEMBER Hello World");
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MEMBER")
    public Mono<String> MEMBER(){
        return Mono.just("MEMBER Hello World");
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MERCHANT")
    public Mono<String> MERCHANT(){
        return Mono.just("MERCHANT Hello World");
    }
}
