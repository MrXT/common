package com.project.common.spring.interceptor;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.common.spring.SessionHolder;
import com.project.common.util.LogUtils;


/**
 * 接口访问权限验证 ClassName: SecurityInterceptor <br/>
 * Package Name:com.karakal.interceptor
 * @author xt(di.xiao@karakal.com.cn)
 * @version 1.0 Date:2016年7月28日上午11:02:41 Copyright (c) 2016, manzz.com All
 *          Rights Reserved.
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
        //printResponseContext(response);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        SessionHolder.setResponse(response);
        SessionHolder.setRequest(request);
//        HttpServletRequest reqeust = (HttpServletRequest) request;
//        printRequestLog(reqeust);
        return true;
    }

    /**
     * 打印请求日志
     * @param request
     */
    public void printRequestLog(HttpServletRequest request) {
        LogUtils.DEBUG("###请求详情日志打印开始###");
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()){
            String header = headers.nextElement();
            LogUtils.DEBUG(header+"："+request.getHeader(header));
        }
        LogUtils.DEBUG("请求完整地址:"+request.getRequestURL().toString());
        LogUtils.DEBUG("请求相对地址："+request.getRequestURI());
        LogUtils.DEBUG("请求查询参数："+request.getQueryString());
        StringBuffer sbparam = new StringBuffer("请求参数：");
        for (String key : request.getParameterMap().keySet()) {
            sbparam.append(key + ":" + String.valueOf(request.getParameter(key)) + "----");
        }
        LogUtils.DEBUG(sbparam.toString());
        LogUtils.DEBUG("-----------------");
    }
    /**
     * 打印响应日志
     * @param request
     */
    @SuppressWarnings("unused")
    private void printResponseContext(HttpServletResponse response) {
        LogUtils.DEBUG("###响应详情日志打印开始###");
        Collection<String> headers = response.getHeaderNames();
        for (String header : headers) {
            LogUtils.DEBUG(header+"："+response.getHeader(header));
        }
        LogUtils.DEBUG("------------------");
    }
}
