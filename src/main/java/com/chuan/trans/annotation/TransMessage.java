package com.chuan.trans.annotation;

import java.lang.annotation.*;

/**
 * @author jc
 * Date:2019/4/28
 * Time:15:30
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface TransMessage {
    String topic() default "";
    String tags() default "";
}
