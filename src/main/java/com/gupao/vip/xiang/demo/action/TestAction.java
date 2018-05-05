package com.gupao.vip.xiang.demo.action;

import com.gupao.vip.xiang.demo.service.ITestService;
import com.gupao.vip.xiang.spring.framework.annotation.Autowired;
import com.gupao.vip.xiang.spring.framework.annotation.Controller;
import com.gupao.vip.xiang.spring.framework.annotation.RequestMapping;
import com.gupao.vip.xiang.spring.framework.webmvc.GPModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * xiang 2018/4/21
 **/
@Controller
@RequestMapping("/test")
public class TestAction {

    @Autowired
    private ITestService testService;
    @RequestMapping("/query")
    public GPModelAndView query(String  name){

        String result=testService.query(name);
        Map<String,Object> model=new HashMap<String,Object>();
        model.put("name",name);
        model.put("data",result);
        return  new GPModelAndView("first.html",model);
    }

}
