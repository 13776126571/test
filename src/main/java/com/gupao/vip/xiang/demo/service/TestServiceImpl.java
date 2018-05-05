package com.gupao.vip.xiang.demo.service;


import com.gupao.vip.xiang.spring.framework.annotation.Autowired;
import com.gupao.vip.xiang.spring.framework.annotation.Service;

/**
 * xiang 2018/4/21
 **/
@Service()
public class TestServiceImpl implements  ITestService{

    @Autowired
    private  IDemoService demoService;
    @Override
    public String query(String name) {
        String str= demoService.get(name);
        System.out.println("----------------------"+str+"---------------------");
        return "==================="+name+"======================";
    }


}
