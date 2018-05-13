package com.gupao.vip.xiang.spring.framework.beans;

import com.gupao.vip.xiang.spring.framework.aop.GPAopConfig;
import com.gupao.vip.xiang.spring.framework.aop.GPAopProxy;
import com.gupao.vip.xiang.spring.framework.core.GPFactoryBean;

/**
 * xiang 2018/4/23
 * spring用来包装原始对象
 **/
public class BeanWrapper extends GPFactoryBean {

    private GPAopProxy aopProxy=new GPAopProxy();

    //还会用到观察者模式  自持事件响应
    private GPBeanPostProcessor GPBeanPostProcessor;



    //包装对象
    private  Object wrapperInstance;
    //原始对象
    private Object orginalInstance;
    public BeanWrapper(Object instance){
        //代理对象
      this.wrapperInstance=aopProxy.getProxy(instance);
      //原生对象
      this.orginalInstance=instance;
    }

    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    //返回代理以后的Class
    public Class<?> getWrappedClass(){
        return wrapperInstance.getClass();
    }

    public GPBeanPostProcessor getBeanPostProcessor() {
        return GPBeanPostProcessor;
    }

    public void setBeanPostProcessor(GPBeanPostProcessor GPBeanPostProcessor) {
        this.GPBeanPostProcessor = GPBeanPostProcessor;
    }

    public void setAopConfig(GPAopConfig aopConfig){
        aopProxy.setConfig(aopConfig);
    }
}
