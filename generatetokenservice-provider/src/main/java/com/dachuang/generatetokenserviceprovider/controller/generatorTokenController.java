package com.dachuang.generatetokenserviceprovider.controller;

import com.dachuang.generatetokenserviceprovider.result.Result;
import com.dachuang.generatetokenserviceprovider.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */

@RestController
public class generatorTokenController {

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/generatortoken/{phoneNumber}/{role}")
    public Result<String> generatorToken(@PathVariable("phoneNumber") String phoneNumber, @PathVariable("role") String role){
        String token = jwtUtil.generateToken(phoneNumber,role);
        return new Result<>(token);
    }
}
