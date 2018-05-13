package com.gupao.vip.xiang.spring.framework.beans;

/**
 * xiang 2018/4/23
 * 存储配置文件中的信息， 相当于保存在内存中的一份配置文件
 **/
public class GPBeanDefinition {

    //类名  全路径
    private  String beanClassName;
    //bean在IOC中的名字
    private String factoryBeanName;
    private boolean lazyInit = false;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}
