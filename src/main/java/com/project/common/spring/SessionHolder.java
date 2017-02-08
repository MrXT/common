
package com.project.common.spring;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.common.constant.SystemConstant;

/**
 * ClassName: SessionHolder <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月31日
 * @version 1.0
 */
public class SessionHolder {
    private static ThreadLocal<HttpServletResponse> response =new ThreadLocal<>();
    private static ThreadLocal<HttpServletRequest> request =new ThreadLocal<>();
    
    public static HttpServletResponse getResponse() {
        return response.get();
    }
    
    public static void setResponse(HttpServletResponse arg) {
        response.set(arg);
    }
    
    public static HttpServletRequest getRequest() {
        return request.get();
    }
    
    public static void setRequest(HttpServletRequest arg) {
        request.set(arg);
    }
    /**
     * 获取当前session用户
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getUser(Class<T> clazz){
        return (T) getSession().getAttribute(SystemConstant.DEFAULT_SESSION_USER);
    }
    /**
     * 获取当前登录用户Id
     * @return
     */
    public static String getId(){
       return (String) getSession().getAttribute(SystemConstant.DEFAULT_ID);
    }
    
    public static HttpSession getSession(){
        return request.get().getSession();
    }
    /**
     * 将该次的增删改查权限放入map中
     * @param map 
     * @param string
     */
    public static void setMenuMap(Map<String, Object> map, String string) {
        @SuppressWarnings("unchecked")
        List<String> urls = (List<String>) getSession().getAttribute(SystemConstant.PASS_URLS);
        map.put("url", string);
        if(urls.contains(string+"/invalid")){
            map.put("invalid", true);
        }else{
            map.put("invalid",false);
        }
        if(urls.contains(string+"/revalid")){
            map.put("revalid", true);
        }else{
            map.put("revalid",false);
        }
        if(urls.contains(string+"/update")){
            map.put("update", true);
        }else{
            map.put("update",false);
        }
        if(urls.contains(string+"/insert")){
            map.put("insert", true);
        }else{
            map.put("insert",false);
        }
    }
}

