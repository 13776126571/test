package com.gupao.vip.xiang.spring.framework.context;

import com.gupao.vip.xiang.spring.framework.annotation.Autowired;
import com.gupao.vip.xiang.spring.framework.annotation.Controller;
import com.gupao.vip.xiang.spring.framework.annotation.Service;
import com.gupao.vip.xiang.spring.framework.beans.BeanDefinition;
import com.gupao.vip.xiang.spring.framework.beans.BeanPostProcessor;
import com.gupao.vip.xiang.spring.framework.beans.BeanWrapper;
import com.gupao.vip.xiang.spring.framework.context.support.BeanDefinitionReader;
import com.gupao.vip.xiang.spring.framework.core.GPBeanFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * xiang 2018/4/22
 * 对应spring中 ClassPathXmlApplicationContext
 **/
public class GPApplicationContext implements GPBeanFactory {

    //配置文件
   private  String[] configLocations;
    //操作配置文件的reader
   private BeanDefinitionReader reader;
   //保存配置信息
   private Map<String,BeanDefinition> beanDefinitionMap= new ConcurrentHashMap<String,BeanDefinition>();
   //存放单例bean
   private Map<String,Object> beanCacheMap=new ConcurrentHashMap<String,Object>();
   //存放所有被代理过的类
   private Map<String,BeanWrapper> beanWrapperMap=new ConcurrentHashMap<String,BeanWrapper>();
    public  GPApplicationContext(String... configLocations){
       this.configLocations=configLocations;
       refresh();
    }

    public  void refresh(){
        //定位
        this.reader=new BeanDefinitionReader(configLocations);

        //加载
        List<String> beanDefinitions=reader.loadBeandefinitions();
        //注册
        doRegisty(beanDefinitions);
        //依赖注入 (layz-init=true的时候) 在这里自动调用getBean()方法
        doAutowrired();
    }

    //依赖注入
    private void doAutowrired() {
        for (Map.Entry<String,BeanDefinition> beanDefinitionEntry:this.beanDefinitionMap.entrySet()){
            String beanName=beanDefinitionEntry.getKey();
            //如果不是延迟加载  自动注入
            if (!beanDefinitionEntry.getValue().isLazyInit()){
                getBean(beanName);
            }
        }

        //暂时先在这里全部注入
        for (Map.Entry<String,BeanWrapper> beanWrapperEntry:this.beanWrapperMap.entrySet()){
            String beanName=beanWrapperEntry.getKey();
            populateBean(beanName,beanWrapperEntry.getValue().getWrappedInstance());
        }
    }

    public void populateBean(String beanName,Object instance){
         Class<?> clazz=instance.getClass();
         // 被Controller  Service 标记的类
         if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))){
             return ;
         }
         Field[] fields=clazz.getDeclaredFields();
         for (Field field:fields){
             //只注入被Autowired标记的字段
            if (!field.isAnnotationPresent(Autowired.class)){
                continue;
            }
            Autowired autowired=field.getAnnotation(Autowired.class);
            String autowiredBeanName=autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName=field.getType().getName();
            }

            field.setAccessible(true);
             try {
                 field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
             } catch (IllegalAccessException e) {
                 e.printStackTrace();
             }

         }
    }

    //注册到IOC容器中
    private void doRegisty(List<String> beanDefinitions) {
        if (beanDefinitions.isEmpty()){
            return;
        }

        for (String  className:beanDefinitions){
           //className 有三种形式
            //1.默认类名   首字母小写
            //2.自定义名字(注解中的value)
            //3.接口注入
            try {
                Class<?> beanClass=Class.forName(className);

                //如果是一个接口，   是不能实例化的
                //用他的实现类
                if (beanClass.isInterface()){  continue;}
                BeanDefinition beanDefinition=reader.registerBean(className);
                if (null!=beanDefinition){
                    //放到容器中
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }

                Class<?>[] classes=beanClass.getInterfaces();
                for (Class clazz:classes){
                    this.beanDefinitionMap.put(clazz.getName(),beanDefinition);
                }
                //至此，容器初始化完毕
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    //读取beanDefinition中的信息
    //通过反射机制实例化一个对象 并返回
    //spring不会把最原始的对象返回回去，  会用BeanWrapper包装一下
    //用到了包装器模式， 保留原来的OOP关系，  对原有对象增强 扩展。 为AOP打基础
    @Override
    public Object getBean(String beanName) {
        //从容器中获取BeanDefinition对象
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
       //生成事件通知
        BeanPostProcessor beanPostProcessor=new BeanPostProcessor();

       Object instance=instantionBean(beanDefinition);
       if (null==instance){
           return null;
       }
       //初始化之前调用一次
        beanPostProcessor.postProcessorBeforeInitialization(instance,beanName);
        BeanWrapper beanWrapper=new BeanWrapper(instance);
        beanWrapper.setBeanPostProcessor(beanPostProcessor);
        this.beanWrapperMap.put(beanName,beanWrapper);
        //初始化以后调用一次
        beanPostProcessor.postProcessorAfterInitialization(instance,beanName);
        //不能在这调用， 依赖还没实例化
       // populateBean(beanName,instance);
        return this.beanWrapperMap.get(beanName).getWrappedInstance();
    }

    //实例化
    public Object instantionBean(BeanDefinition beanDefinition){
        Object instance=null;
        String className=beanDefinition.getBeanClassName();
        //如果className存在  直接取
        try {
            if (beanCacheMap.containsKey(className)) {
                instance = beanCacheMap.get(className);
            } else {
                //否则 new一个
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                beanCacheMap.put(className, instance);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }



    public String[] getBeanDefinitionNames(){
        return  this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return  this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfit();
    }
}
