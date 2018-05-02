package com.gupao.vip.xiang.spring.framework.beans;

import com.gupao.vip.xiang.spring.framework.core.GPFactoryBean;

/**
 * xiang 2018/4/23
 * spring用来包装原始对象
 **/
public class BeanWrapper extends GPFactoryBean {

    //还会用到关擦者模式  自持事件响应
    private BeanPostProcessor beanPostProcessor;



    //包装对象
    private  Object wrapperInstance;
    //原始对象
    private Object orginalInstance;
    public BeanWrapper(Object instance){
      this.wrapperInstance=instance;
      this.orginalInstance=instance;
    }

    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    //返回代理以后的Class
    public Class<?> getWrappedClass(){
        return wrapperInstance.getClass();
    }

    public BeanPostProcessor getBeanPostProcessor() {
        return beanPostProcessor;
    }

    public void setBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessor = beanPostProcessor;
    }
}
