package com.gupao.vip.xiang.spring.framework.aop;

import javafx.beans.binding.ObjectExpression;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * xiang 2018/5/13
 **/
public class GPAopProxyUtils {

    //根据代理对象获取原始的对象
    public static Object getTargetObject(Object proxy)throws  Exception{

        //判断是不是代理过的对象
        if (!isAopRroxy(proxy)){
            //如果不是一个代理对象，直接返回
            return  proxy;
        }
        return getProxyTargetObject(proxy);
    }

    //判断是否为代理过的对象
    private  static  boolean isAopRroxy(Object object){
        return Proxy.isProxyClass(object.getClass());
    }


    private static  Object getProxyTargetObject(Object proxy)throws Exception{

        //spring中 h 保存了原始的对象
       Field h= proxy.getClass().getSuperclass().getDeclaredField("h");
       h.setAccessible(true);
       GPAopProxy aopProxy=(GPAopProxy) h.get(proxy);
       Field target=aopProxy.getClass().getDeclaredField("target");
       target.setAccessible(true);
        return target.get(aopProxy);
    }
}
