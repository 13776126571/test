package com.gupao.vip.xiang.spring.framework.webmvc;

import com.gupao.vip.xiang.spring.framework.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * xiang 2018/4/22
 **/
public class DispatcherServlet extends HttpServlet {

    //HandlerMapping是SpringMVC最核心最经典的设计， 牛逼到干掉了strts等MVC框架
    private List<GPHandlerMapping> handlerMapping= new ArrayList<GPHandlerMapping>();

    //
    private List<GPHandlerAdapter> handlerAdapters=new ArrayList<GPHandlerAdapter>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req,resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {

        GPHandlerMapping handler= getHandler(req);
        GPHandlerAdapter ha= getHandlerAdapter(handler);

        GPModelAndView mv= ha.handler(req,resp,ha);

        processDispathcResult(req,mv);
    }

    private void processDispathcResult(HttpServletRequest req, GPModelAndView mv) {
        //调用 ViewResolver的 resolveView() 方法
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {
        return null;
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //相当于把IOC容器初始化了
        GPApplicationContext gpApplicationContext=new GPApplicationContext();

        initStrategies(gpApplicationContext);

    }

    private void initStrategies(GPApplicationContext applicationContext) {

        //通过HandlerMapping 将请求影射到控制器
        //HandlerMapping用来保存Controller中配置的RequestMapping和Method的一个对应关系
         initHandlerMappings(applicationContext);
        //通过HandlerAdapter 进行多类型参数动态匹配
        //HandlerAdapter 用来动态匹配Method参数，包括类型转换，动态赋值
        initHandlerAdapters(applicationContext);
        //通过ViewResolver解析逻辑视图到具体视图实现
        //通过ViewResolver实现动态模板解析
        initViewResolvers(applicationContext);
    }

    private void initViewResolvers(GPApplicationContext applicationContext) {

    }

    private void initHandlerAdapters(GPApplicationContext applicationContext) {

    }

    //HandlerMapping用来保存Controller中配置的RequestMapping和Method 一一对应
    private void initHandlerMappings(GPApplicationContext applicationContext) {
        //通常 是一个Map<String,Method>   map.put(url,Method);
        //但是 GPHandlerAdapter有个Pattern属性 已经包含了url信息， 所以直接用list<GPHandlerAdapter>
    }
}
