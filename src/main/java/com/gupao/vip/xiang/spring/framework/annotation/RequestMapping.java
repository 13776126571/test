package com.gupao.vip.xiang.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * xiang 2018/4/21
 **/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default  "";
}
