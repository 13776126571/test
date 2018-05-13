package com.gupao.vip.xiang.spring.framework.beans;

/**
 * xiang 2018/4/23
 * 用于做事件监听
 **/
public class GPBeanPostProcessor {

    //之前
    public Object postProcessorBeforeInitialization(Object bean,String className){
        return bean;
    }

    //之后
    public Object postProcessorAfterInitialization(Object bean,String className){
        return bean;
    }
}
