package com.dachuang.signserviceprovider.controller;

import com.dachuang.signserviceprovider.pojo.SignVo;
import com.dachuang.signserviceprovider.result.Result;
import com.dachuang.signserviceprovider.service.SignService;
import com.dachuang.signserviceprovider.validator.annotation.IsMobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
@RestController
public class SignController {

    private static final Logger log = LoggerFactory.getLogger(SignController.class);

    @Resource
    private SignService signService;

    @PostMapping("/sharedkitchen/sign")
    public Result<String> sign(@RequestBody @Validated SignVo signVo){
        log.info("[SignController ---- SignVo]{}",signVo.toString());
        signService.doSign(signVo);
        return Result.signSuccess();
    }

    @RequestMapping(value = "/sharedkitchen/smssendkaptcha/{phoneNumber}",method = RequestMethod.GET)
    public Result<String> smsSendKaptcha(@PathVariable @IsMobile String phoneNumber){
        //send auth code service
        return new Result<>("send auth code success");
    }

    @GetMapping("/test")
    public String test(){
        return "HelloWorld";
    }

}
