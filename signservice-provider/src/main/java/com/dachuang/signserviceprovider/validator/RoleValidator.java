package com.dachuang.signserviceprovider.validator;

import com.dachuang.signserviceprovider.exception.BaseException;
import com.dachuang.signserviceprovider.util.ValidatorUtils;
import com.dachuang.signserviceprovider.validator.annotation.IsLegalRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/16
 */

public class RoleValidator implements ConstraintValidator<IsLegalRole,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value))
            throw new BaseException("role must be not null");
        return ValidatorUtils.checkRole(value);
    }
}
