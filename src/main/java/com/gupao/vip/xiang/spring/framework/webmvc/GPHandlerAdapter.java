package com.gupao.vip.xiang.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public GPModelAndView handler(HttpServletRequest req, HttpServletResponse resp, GPHandlerAdapter handle) {
        //根据用户请求的参数信息， 跟method中的参数信息 要进行动态匹配

        //只有当用户传过来的 ModelAndView为空的时候   才会new一个ModelAndView
        return null;
    }

}
