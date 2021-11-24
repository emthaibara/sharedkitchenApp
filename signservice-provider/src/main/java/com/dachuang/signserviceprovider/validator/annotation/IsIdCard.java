package com.dachuang.signserviceprovider.validator.annotation;


import com.dachuang.signserviceprovider.validator.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {IdCardValidator.class} //校验类
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface IsIdCard {

    String message() default "身份证格式不合法";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
