package com.gupao.vip.xiang.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * xiang 2018/4/21
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default  "";
}
