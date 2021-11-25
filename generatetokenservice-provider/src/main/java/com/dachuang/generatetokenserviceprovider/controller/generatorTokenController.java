package com.dachuang.generatetokenserviceprovider.controller;

import com.dachuang.generatetokenserviceprovider.result.Result;
import com.dachuang.generatetokenserviceprovider.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/generatortoken")
    public Result<String> generatorToken(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("role") String role){
        String token = jwtUtil.generateToken(phoneNumber,role);
        return new Result<>(token);
    }
}
