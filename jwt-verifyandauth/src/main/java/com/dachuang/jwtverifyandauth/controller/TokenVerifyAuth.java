package com.dachuang.jwtverifyandauth.controller;

import com.dachuang.jwtverifyandauth.result.Result;
import com.dachuang.jwtverifyandauth.service.VerifyAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */

@RestController
public class TokenVerifyAuth {

    @Resource
    private VerifyAuthService verifyAuthService;

    @GetMapping("/verify-auth/{token}")
    public Result<?> verify(@PathVariable String token){
        Boolean verifyResult = verifyAuthService.Verify(token);
        if (!verifyResult){
            return new Result<>().error("token error",null);
        }
        return new Result<>(verifyAuthService.auth(token));
    }
}
