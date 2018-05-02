package com.gupao.vip.xiang.spring.servlet;

import com.gupao.vip.xiang.spring.framework.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * xiang 2018/4/22
 * Servlet只是作为一个MVC启动的入口
 **/
public class DispatchServlet  extends HttpServlet {

    public  final  String LOCATION="contextConfigLocation";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        GPApplicationContext gpApplicationContext=new GPApplicationContext(config.getInitParameter(LOCATION));
    }
}
