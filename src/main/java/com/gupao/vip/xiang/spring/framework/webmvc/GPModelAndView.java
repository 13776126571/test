package com.gupao.vip.xiang.spring.framework.webmvc;

import java.util.Map;

/**
 * xiang 2018/4/24
 **/
public class GPModelAndView {


    private String viewName;
    private Map<String,?> model;

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }

    public GPModelAndView(String viewName, Map<String,?> model){

        this.viewName=viewName;
        this.model=model;
    }


}
