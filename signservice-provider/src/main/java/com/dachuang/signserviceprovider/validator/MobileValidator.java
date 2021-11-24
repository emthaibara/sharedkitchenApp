package com.dachuang.signserviceprovider.validator;

import com.dachuang.signserviceprovider.exception.BaseException;
import com.dachuang.signserviceprovider.util.ValidatorUtils;
import com.dachuang.signserviceprovider.validator.annotation.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/16
 */
public class MobileValidator implements ConstraintValidator<IsMobile,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value))
            throw new BaseException("mobile must be not null");
        //采用正则表达式，校验手机号
        return ValidatorUtils.checkMobile(value);
    }
}
