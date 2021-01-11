package com.zwx.common.vaild;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author coderZWX
 * @date 2021-01-05 20:05
 */
@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})//【可以指定多个不同的效验器，适配不同类型的效验】
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {

    // 三要素不能丢
    String message() default "{com.zwx.common.valid.ListValue.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] vals() default { };

}
