package com.dachuang.signserviceprovider.advice;

import com.dachuang.signserviceprovider.dao.RegisteredSchoolMapper;
import com.dachuang.signserviceprovider.util.ValidatorUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/12
 */
@Aspect
@Component
public class GlobalAopAdvice {

    @Resource
    private RegisteredSchoolMapper registeredSchoolMapper;

    private static final Logger log = LoggerFactory.getLogger(GlobalAopAdvice.class);

    @After("com.dachuang.signserviceprovider.aspect.SystemArchitecture.checkSchoolPointcut()")
    public void assemblyRegisteredSchoolMapper(){
        log.info("GlobalAopAdvice -- assemblyRegisteredSchoolMapper");
        ValidatorUtils.initialRegisteredSchoolMapper(registeredSchoolMapper);
    }
}
