package com.gupao.vip.xiang.spring.framework.webmvc;

/**
 * xiang 2018/4/24
 **/

import java.io.File;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类的主要作用：
 * 1.将一个静态文件变成一个动态文件
 *   根据用户传的参数，产生不同的结果
 *   最终输出字符串，交给response输出
 */
public class GPViewResolver {

    private  String viewName;
    private  File templateFile;
    public  GPViewResolver(String viewName, File templateFile){
        this.viewName=viewName;
        this.templateFile=templateFile;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public String viewResolve(GPModelAndView mv)throws  Exception{
        StringBuffer sb=new StringBuffer();
        //读取模板文件中的内容
        RandomAccessFile ra= new RandomAccessFile(this.templateFile,"r");

        try {
            String line = null;
            while (null != (line = ra.readLine())) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                Matcher m = matcher(line);
                while (m.find()) {
                    for (int i = 1; i <= m.groupCount(); i++) {

                        //要把￥{}中间的这个字符串给取出来
                        String paramName = m.group(i);
                        Object paramValue = mv.getModel().get(paramName);
                        if (null == paramValue) {
                            continue;
                        }
                        line = line.replaceAll("￥\\{" + paramName + "\\}", paramValue.toString());
                        line = new String(line.getBytes("utf-8"), "ISO-8859-1");
                    }
                }
                sb.append(line);
            }
        }finally {
            ra.close();
        }

        return  sb.toString();
    }

    private Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return  matcher;
    }
}
