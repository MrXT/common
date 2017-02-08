package com.project.common.spring.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.common.spring.SessionHolder;
import com.project.common.util.LogUtils;

public class PathFilter implements Filter {

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (!servletRequest.getRequestURI().contains(".")) {//判断是不是静态资源的请求
            StringBuffer sbparam = new StringBuffer("请求地址：" + servletRequest.getRequestURI() + ",请求参数：");
            for (String key : request.getParameterMap().keySet()) {
                sbparam.append(key + "=" + String.valueOf(request.getParameter(key)) + "&");
            }
            LogUtils.DEBUG(sbparam.toString());
        }
        SessionHolder.setResponse((HttpServletResponse) arg1);
        SessionHolder.setRequest(servletRequest);
        arg2.doFilter(request, arg1);

    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
