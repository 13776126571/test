package com.gupao.vip.xiang.spring.framework.context.support;

import com.gupao.vip.xiang.spring.framework.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * xiang 2018/4/23
 * 对配置文件进行定位，读取，解析
 **/
public class BeanDefinitionReader {

    private  Properties config=new Properties();
    private List<String> beanClasses=new ArrayList<String>();
    public  BeanDefinitionReader(String... locations){
        //加载配置文件
        InputStream is=this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //扫描指定包下的类
        doScanner(config.getProperty("scanPackage"));

    }


    public List<String> loadBeandefinitions(){
        return  this.beanClasses;
    }
    //每注册一个className, 就返回一个BeanDefinition
    public BeanDefinition registerBean(String className)  {
        if (beanClasses.contains(className)){
            BeanDefinition beanDefinition=new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".") + 1)));
            return beanDefinition;
        }
         return  null;
    }

    public Properties getConfit(){
        return this.config;
    }

    //递归扫描指定包下的文件   并保存到list中
    public void doScanner(String packgeName){
        URL url=this.getClass().getClassLoader().getResource("/"+packgeName.replaceAll("\\.","/"));
        File classDir=new File(url.getFile());

        for (File file:classDir.listFiles()){
            if (file.isDirectory()){
                doScanner(packgeName+"."+file.getName());
            }else{
                beanClasses.add(packgeName+"."+file.getName().replace(".class",""));
            }
        }
    }


    //字符串首字母小写
    private String lowerFirstCase(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
