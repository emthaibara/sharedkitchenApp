package com.dachuang.signserviceprovider.util;

import com.dachuang.signserviceprovider.dao.RegisteredSchoolMapper;
import com.dachuang.signserviceprovider.pojo.RegisteredSchool;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/16
 */
public class ValidatorUtils {

    private static final List<String> ROLES = Arrays.asList("NONMEMBER","MEMBER","MERCHANT");

    private static RegisteredSchoolMapper registeredSchoolMapper;

    public static void initialRegisteredSchoolMapper(RegisteredSchoolMapper registeredSchoolMapper){
        ValidatorUtils.registeredSchoolMapper = registeredSchoolMapper;
    }

    public static Boolean checkSchool(String school){
        return registeredSchoolMapper.selectAll().contains(new RegisteredSchool(school));
    }

    public static Boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }

    public static Boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]";
        return Pattern.matches(regex,idCard);
    }

    public static boolean checkRole(String value) {
        return ROLES.contains(value);
    }
}
