package com.gupao.vip.xiang.demo.service;

import com.gupao.vip.xiang.spring.framework.annotation.Service;

/**
 * xiang 2018/4/22
 **/
@Service("demoServiceImpl")
public class DemoServiceImpl implements  IDemoService {
    @Override
    public String get(String name) {
        return name;
    }
}
