package com.dachuang.signserviceprovider.validator.annotation;


import com.dachuang.signserviceprovider.validator.SchoolValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {SchoolValidator.class} //校验类
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRegisteredSchool {

    String message() default "该学校尚未注册";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
