package com.gupao.vip.xiang.spring.framework.context;

import com.gupao.vip.xiang.spring.framework.beans.GPBeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * xiang 2018/5/12
 **/
public class GPDefaultListableBeanFactory extends  GPAbstractApplicationContext {


    //用来保存配置信息
    protected Map<String,GPBeanDefinition> beanDefinitionMap=new HashMap<String,GPBeanDefinition>();
    protected void onRefresh() {

    }

    @Override
    protected void refreshBeanFactory() {

    }
}
