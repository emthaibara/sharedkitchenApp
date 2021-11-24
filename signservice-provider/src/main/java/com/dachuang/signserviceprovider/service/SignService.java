package com.dachuang.signserviceprovider.service;


import com.dachuang.signserviceprovider.dao.UserMapper;
import com.dachuang.signserviceprovider.exception.BaseException;
import com.dachuang.signserviceprovider.pojo.SignVo;
import com.dachuang.signserviceprovider.pojo.User;
import com.dachuang.signserviceprovider.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */

@Service
public class SignService {

    private static final Logger log = LoggerFactory.getLogger(SignService.class);

    @Resource
    private UserMapper userMapper;

    private static final String DEFAULT_INTRODUCTION = "welcome to sharedkitchen app";
    private static final String DEFAULT_USERNAME = "Jack";

    public void doSign(SignVo signVo){
        if (!isExistMobile(signVo.getPhoneNumber())){
            //快速返回
            throw new BaseException("该手机号已被注册");
        }
        if (!isExistIdCard(signVo.getIdCard())){
            //快速返回
            throw new BaseException("该身份证已被注册");
        }
        User record = getRecord(signVo);
        userMapper.insert(record);
        log.info("[Shard Kitchen app user +1--------]{}", record);
    }

    private User getRecord(SignVo signVo){
        User record = new User();
        record.setIdcard(signVo.getIdCard());
        record.setPassword(MD5Util.formPassToDBPass(signVo.getPassword()));//加密password
        record.setSchool(signVo.getSchool());
        record.setPhonenumber(signVo.getPhoneNumber());
        record.setRole(signVo.getRole());
        record.setIntroduction(DEFAULT_INTRODUCTION);
        record.setUsername(DEFAULT_USERNAME);
        return record;
    }

    /**
     * 判断用户是否已经注册
     * @param phoneNumber phoneNumber-key
     * @return  null --> true --> not exist(false)
     *          not null --->false ---> exit(true)
     */
    private Boolean isExistMobile(String phoneNumber){
        return Objects.isNull(userMapper.selectByPrimaryKey(phoneNumber));
    }

    private Boolean isExistIdCard(String idCard){
        return Objects.isNull(userMapper.selectByPrimaryKeyIdCard(idCard));
    }
}
