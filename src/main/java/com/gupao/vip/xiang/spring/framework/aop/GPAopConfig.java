package com.gupao.vip.xiang.spring.framework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * xiang 2018/5/12
 **/

//对 application中的切面表达式封装
    //对目标对象方法的增强
    //配置文件的作用：告诉spring，哪些类需要增强，增强内容是什么
    //对配置文件中的内容进行封装
public class GPAopConfig {

    //以目标对象的metho作为key，需要增强的内容作为value
    private Map<Method,GPAspect> points=new HashMap<Method, GPAspect>();

    public void put(Method target,Object aspect,Method[] points){
        this.points.put(target,new GPAspect(aspect,points));
    }

    public GPAspect get(Method method){
        return this.points.get(method);
    }

    public boolean contains(Method method){
        return  this.points.containsKey(method);
    }

    //对增强的内容的封装
    public class GPAspect{
        private Object aspect;//将LogAspect赋值给它
        private Method[] points;//把LogAspect的before和after方法赋值给它

        public GPAspect(Object aspect, Method[] points) {
            this.aspect = aspect;
            this.points = points;
        }

        public Object getAspect() {
            return aspect;
        }

        public Method[] getPoints() {
            return points;
        }
    }
}
