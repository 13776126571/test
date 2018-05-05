package com.gupao.vip.xiang.spring.framework.webmvc;

/**
 * xiang 2018/4/24
 **/

import java.io.File;

/**
 * 这个类的主要作用：
 * 1.将一个静态文件变成一个动态文件
 *   根据用户传的参数，产生不同的结果
 *   最终输出字符串，交给response输出
 */
public class GPViewResolver {

    private  String viewName;
    private  File templateFile;
    public  GPViewResolver(String viewName, File templateFile){
        this.viewName=viewName;
        this.templateFile=templateFile;
    }

    public String viewResolve(GPModelAndView mv){
        return  null;
    }
}
