package com.gupao.vip.xiang.spring.framework.webmvc;

import javafx.beans.binding.ObjectExpression;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * xiang 2018/4/24
 **/
public class GPHandlerAdapter {
    /**
     *
     * @param req
     * @param resp 传不传都无所谓，由用户决定    Request Response 是从Servlet中获得的   无法new
     *               传进来目的只有一个：将Response赋给方法 参数
     * @param handle   为什么要把handle传进来
     *                  因为handle中包含了 controller ,method,url信息
     * @return
     */

    //形参列表
    private  Map<String,Integer> paramMapping;

    public GPHandlerAdapter(Map<String,Integer> paramMapping){
        this.paramMapping=paramMapping;
    }
    public GPModelAndView handler(HttpServletRequest req, HttpServletResponse resp, GPHandlerMapping handle) throws InvocationTargetException, IllegalAccessException {
        //根据用户请求的参数信息， 跟method中的参数信息 要进行动态匹配

        //只有当用户传过来的 ModelAndView为空的时候   才会new一个ModelAndView

        //方法形参列表
       Class<?>[]paramTypes= handle.getMethod().getParameterTypes();

        //拿到自定义命名参数的位置
        //拿到用户通过URL传过来的参数列表
        Map<String,String []>map=req.getParameterMap();
        //构造实参列表
        Object[]paramValues=new Object[paramTypes.length];
        for (Map.Entry<String,String[]> param :map.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s","");
            if (!this.paramMapping.containsKey(param.getKey())){continue;}
            //参数位置
            int index=this.paramMapping.get(param.getKey());
            //页面传过来的参数类型都是String类型的。  方法中的参数类型有很多
            //这里要做类型转换
            paramValues[index]=caseStringValue(value,paramTypes[index]);
        }
        //给 request和response赋值
        if(this.paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = this.paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }

        if(this.paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = this.paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        //从handle中拿到controller,method,然后利用反射机制进行调用

       Object result= handle.getMethod().invoke(handle.getController(),paramValues);

       if (result==null){
           return null;
       }
       //判断方法的返回类型是不是 ModelAndView
       Boolean isModelAndView= handle.getMethod().getReturnType()==GPModelAndView.class;
       if (isModelAndView){
           return  (GPModelAndView) result;
       }else{
           return  null;
       }

    }


    private Object caseStringValue(String value,Class<?> clazz){

        if (clazz==String.class){
            return value;
        }
        if (clazz==Integer.class){
            return  Integer.valueOf(value);
        }
        if (clazz==int.class){
            return  Integer.valueOf(value).intValue();
        }

        return  null;
    }
}
