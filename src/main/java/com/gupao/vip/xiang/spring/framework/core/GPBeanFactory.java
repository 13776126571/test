package com.gupao.vip.xiang.spring.framework.core;

/**
 * xiang 2018/4/22
 **/
public interface GPBeanFactory {
    //根据beanName 从容器总中获得一个Bean实例
    public  Object getBean(String beanName) throws Exception;
}
