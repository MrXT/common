package com.project.common.spring.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.common.constant.SystemConstant;
import com.project.common.spring.SessionHolder;
import com.project.common.util.CommonUtils;
import com.project.common.util.ValidateUtils;
import com.project.entity.Menu;
import com.project.module.sys.controller.vo.UserVO;

public class ProjectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPermitedUrl(request.getRequestURI())) {// 是否是直接能访问的url
            return true;
        }
        // 解决IFRAME中SESSION无法保留的问题
        response.setHeader("P3P", "CP=CAO PSA OUR");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        if (CommonUtils.hasBlank(SessionHolder.getSession(), SessionHolder.getUser(UserVO.class))) {
            if (request.getRequestURI().replace("//", "/").replace("/", "").toLowerCase().endsWith("manage")) {
                response.sendRedirect(basePath + "login");
                return false;
            }
            response.sendRedirect(basePath + "common/nosession");
            return false;
        }
        UserVO userVo = SessionHolder.getUser(UserVO.class);
        if (!hasMenu(userVo.getMenus(), request.getRequestURI())) {
            Boolean can = false;
            String appStr = request.getParameter("app");// APP接口访问必须传app=true
            if (ValidateUtils.isNotBlank(appStr)) {
                if (Boolean.parseBoolean(appStr)) {// 判断是不是app的请求
                    if (isSystemUrl(request.getRequestURI())) {// 系统级资源不能APP用户不能操作
                        can = false;
                    } else {// APP用户只能 增删改 自己的数据，个人级资源:APP用户只能查自己的数据
                        if (ValidateUtils.isNotBlank(request.getParameter("requstForward"))) {
                            return true;
                        }
                        if (!request.getRequestURI().contains("/query") || isUserUrl(request.getRequestURI())) {
                            if (ValidateUtils.isNotBlank(request.getParameter("userId"))
                                && !CommonUtils.compareString(request.getParameter("userId"), SessionHolder.getId(), true)) {// 判断请求有没有userId,如果有，则判断是否与登录用户的id一样，不一样就提示没有权限
                                can = false;
                            } else if (ValidateUtils.isNotBlank(request.getParameter("userId"))
                                && CommonUtils.compareString(request.getParameter("userId"), SessionHolder.getId(), true)) {
                                can = true;
                            } else {
                                String queryParam = "userId=" + SessionHolder.getId() + "&requstForward=" + true;
                                request.getRequestDispatcher(request.getRequestURI() + "?" + queryParam).forward(request, response);
                                return false;
                            }
                        } else if(request.getRequestURI().contains("/query")){// 所有的APP用户只能 增删改 自己的数据,但能查询别人的数据
                            return true;
                        } else{
                            return false;
                        }

                    }
                }
            }
            if (!can) {
                response.sendRedirect(basePath + "common/noauth");
                return false;
            }
        }
        return true;
    }

    /**
     * @param menus 菜单
     * @param url 地址
     * @return
     */
    private boolean hasMenu(List<Menu> menus, String url) {
        String path = url.replace("/", "").replace("\\", "");
        for (Menu menu : menus) {
            if (ValidateUtils.isNotBlank(menu.getMenuUrl()) && path.contains(menu.getMenuUrl().replace("/", ""))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /** 判断是否为允许直接访问的url */
    private boolean isPermitedUrl(String url) {
        String path = url.replace("\\", "").replace("/", "");
        String[] permitedUrls = SystemConstant.PERMITED_URLS;
        for (String permitedUrl : permitedUrls) {
            if(permitedUrl.endsWith("/**")){//例如tool/**
                if(path.contains(permitedUrl.replace("/**", ""))){//只要path包含tool，就可以通过
                    return true;
                }
            }
            if (path.endsWith(permitedUrl.replace("/", "")))
                return true;
        }
        return false;
    }

    /** 判断是否为系统级资源url */
    private boolean isSystemUrl(String url) {
        String path = url.replace("\\", "").replace("/", "");
        String[] permitedUrls = SystemConstant.SYSTEM_URLS;
        for (String permitedUrl : permitedUrls) {
            if (path.contains(permitedUrl.replace("/", "")))
                return true;
        }
        return false;
    }

    /** 判断是否为个人级资源url */
    private boolean isUserUrl(String url) {
        String path = url.replace("\\", "").replace("/", "");
        String[] permitedUrls = SystemConstant.USER_URLS;
        for (String permitedUrl : permitedUrls) {
            if (path.contains(permitedUrl.replace("/", "")))
                return true;
        }
        return false;
    }
}
