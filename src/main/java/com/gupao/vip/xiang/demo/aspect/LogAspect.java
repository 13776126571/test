package com.gupao.vip.xiang.demo.aspect;

/**
 * xiang 2018/5/12
 **/
public class LogAspect {

    //调用方法之前 执行这个方法
    public void before(){
        System.out.println("========================调用之前=========================");

    }
    //调用方法之后 执行这个方法
    public void after(){
        System.out.println("========================调用之后=========================");

    }
}
