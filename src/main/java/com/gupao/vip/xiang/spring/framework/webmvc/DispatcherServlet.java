package com.gupao.vip.xiang.spring.framework.webmvc;

import com.gupao.vip.xiang.spring.framework.annotation.Controller;
import com.gupao.vip.xiang.spring.framework.annotation.RequestMapping;
import com.gupao.vip.xiang.spring.framework.annotation.RequestParam;
import com.gupao.vip.xiang.spring.framework.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xiang 2018/4/22
 **/
public class DispatcherServlet extends HttpServlet {

    private  final String LOCATION = "contextConfigLocation";
    //HandlerMapping是SpringMVC最核心最经典的设计， 牛逼到干掉了strts等MVC框架
    //保存Controller中配置的RequestMapping和Method的一个对应关系
    private List<GPHandlerMapping> handlerMappings= new ArrayList<GPHandlerMapping>();

    //用来动态匹配Method参数，包括类型转换，动态赋值
    private Map<GPHandlerMapping,GPHandlerAdapter> handlerAdapters=new HashMap<GPHandlerMapping,GPHandlerAdapter>();

    //视图列表
    private List<GPViewResolver> viewResolvers=new ArrayList<GPViewResolver>();


    @Override
    public void init(ServletConfig config) throws ServletException {

        //相当于把IOC容器初始化了
        GPApplicationContext gpApplicationContext=new GPApplicationContext(config.getInitParameter(LOCATION));

        //初始化 handlerMapping  handlerAdapter viewResolver
        initStrategies(gpApplicationContext);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String url=req.getRequestURI();
//        String contextPath=req.getContextPath();
//        url=url.replace(contextPath,"").replaceAll("/+","/");

        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            doDispatch(req, resp);
        }catch (Exception e){
            resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s","\r\n") +  "<font color='green'><i>Copyright@GupaoEDU</i></font>");
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp)throws  Exception {

            //根据用户请求的URL 获得一个handler
            GPHandlerMapping handler = getHandler(req);
            if (handler==null){
                resp.getWriter().write("<font size='25' color='red'>404 Not Found</font><br/><font color='green'><i>Copyright@GupaoEDU</i></font>");
                return;
            }
            GPHandlerAdapter ha = getHandlerAdapter(handler);

            //调用方法，得到返回值ModelAndView
            GPModelAndView mv = ha.handler(req, resp, handler);

            processDispathcResult(resp, mv);

    }

    private void processDispathcResult(HttpServletResponse resp, GPModelAndView mv) throws Exception {
        //调用 ViewResolver的 resolveView() 方法
        if(null==mv){return;}
        if (this.viewResolvers.isEmpty()){return; }
        for (GPViewResolver viewResolver:viewResolvers){

            if (!mv.getViewName().equals(viewResolver.getViewName())){
                continue;
            }
            String out=viewResolver.viewResolve(mv);
            if (out !=null){
                resp.getWriter().write(out);
                break;
            }
        }
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()){return  null;}
        return this.handlerAdapters.get(handler);
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {

        if (this.handlerMappings.isEmpty()){return  null;}
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");

        //去handlerMapping中匹配输入的url
        for (GPHandlerMapping handler:handlerMappings){
            Matcher matcher=handler.getPattern().matcher(url);
            if (!matcher.matches()){continue;}
            return  handler;
        }
        return null;
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

        //在页面输入  https://localhost/first.html
        //页面名字和模板文件关联
       String templateRoot= applicationContext.getConfig().getProperty("templateRoot");
      String templateRootPath= this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateDir=new File(templateRootPath);
        for (File file:templateDir.listFiles()){
            this.viewResolvers.add(new GPViewResolver(file.getName(),file));
        }

    }

    private void initHandlerAdapters(GPApplicationContext applicationContext) {

        //在初始化阶段 能做的就是将参数的名字或者类型按顺序保存下来
        //后面用反射调用的时候传入的形参是一个数组
        //通过记录这些参数的位置，按个赋值
        for (GPHandlerMapping handlerMapping:this.handlerMappings){
            //每个方法有个参数列表。这里保存的是形参列表
            Map<String,Integer> paramMapping=new HashMap<String,Integer>();
            //每个参数可以加多个注解
            Annotation [][] pa=handlerMapping.getMethod().getParameterAnnotations();


            for (int i=0;i<pa.length;i++){
                for (Annotation a:pa[i]){
                    if (a instanceof RequestParam){
                        String paramName=((RequestParam) a).value();
                        //把形参的名字和位置放到map中
                        if (!"".equals(paramName.trim())){
                            paramMapping.put(paramName,i);
                        }
                    }
                }
            }

            // 获取方法的参数名
            Parameter[] parameter=handlerMapping.getMethod().getParameters();

            for (int i=0;i<parameter.length;i++){
                System.out.println("========"+parameter[i].getName());

            }
            //处理非命名参数
            //只处理request 和response
            Class<?>[] paramTypes=handlerMapping.getMethod().getParameterTypes();
            for (int i=0; i<paramTypes.length;i++){
                Class<?>type=paramTypes[i];
                if (type==HttpServletRequest.class || type==HttpServletResponse.class){
                    paramMapping.put(type.getName(),i);
                }
            }

            this.handlerAdapters.put(handlerMapping,new GPHandlerAdapter(paramMapping));

        }

    }

    //HandlerMapping用来保存Controller中配置的RequestMapping和Method 一一对应
    private void initHandlerMappings(GPApplicationContext applicationContext) {
        //通常 是一个Map<String,Method>   map.put(url,Method);
        //但是 GPHandlerAdapter有个Pattern属性 已经包含了url信息， 所以直接用list<GPHandlerAdapter>


        String[]beanNames= applicationContext.getBeanDefinitionNames();
        for (String beanName:beanNames){
            //从容器中取到所有的实例
            System.out.println("============================"+beanName+"=============================");
            //获取到的是被包装过的对象，不是原始的对象a
            Object controller=applicationContext.getBean(beanName);
            Class<?>clazz=controller.getClass();
            //只要带Controller注解的
            if (!clazz.isAnnotationPresent(Controller.class)){continue;}

            String baseUrl="";
            //带RequestMapping注解的
            if (clazz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping requestMapping=clazz.getAnnotation(RequestMapping.class);
                baseUrl=requestMapping.value();
            }
            //获取所有的public方法
            Method[] methods=clazz.getMethods();
            for (Method method:methods){
                //只扫描带有RequestMapping的方法
                if (!method.isAnnotationPresent(RequestMapping.class)){continue;}
                RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
                String regex=(baseUrl+requestMapping.value().replaceAll("/+","/"));
                Pattern pattern=Pattern.compile(regex);
                this.handlerMappings.add(new GPHandlerMapping(pattern,controller,method));
                System.out.println("Mapping"+regex+","+method);

            }

        }
    }
}
