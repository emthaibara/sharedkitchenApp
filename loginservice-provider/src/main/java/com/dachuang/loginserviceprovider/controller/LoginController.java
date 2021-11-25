package com.dachuang.loginserviceprovider.controller;

import com.dachuang.loginserviceprovider.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/25
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    public Result<String> login(){
        return Result.signSuccess();
    }

}
