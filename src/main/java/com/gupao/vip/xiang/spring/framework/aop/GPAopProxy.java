package com.gupao.vip.xiang.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * xiang 2018/5/12
 **/
//默认用JDK动态代理
public class GPAopProxy  implements InvocationHandler {

    //AOP配置文件
    private GPAopConfig config;
    //被代理对象
    private Object target;

    public  void setConfig(GPAopConfig config){
        this.config=config;
    }


    //把原生对象传进来
   public Object getProxy(Object instance){
     this.target=instance;
     Class<?>clazz=instance.getClass();
     return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //原生方法
        Method m=this.target.getClass().getMethod(method.getName(),method.getParameterTypes());

        //在原始方法调用之前 执行
        //要通过原生的方法找， 通过代理的方法在map中是找不到的
        if (config.contains(m)){
            GPAopConfig.GPAspect aspect= config.get(m);
            aspect.getPoints()[0].invoke(aspect.getAspect());
        }

        //反射调用原始方法
        Object obj=method.invoke(this.target,args);
        //在原始方法调用之后 执行
        if (config.contains(m)){
            GPAopConfig.GPAspect aspect= config.get(m);
            aspect.getPoints()[1].invoke(aspect.getAspect());
        }
        //将原始的返回值返回
        return obj;
    }
}
