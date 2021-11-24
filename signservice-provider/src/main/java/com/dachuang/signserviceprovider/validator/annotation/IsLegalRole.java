package com.dachuang.signserviceprovider.validator.annotation;


import com.dachuang.signserviceprovider.validator.RoleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {RoleValidator.class} //校验类
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface IsLegalRole {
    String message() default "不存在该角色";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
