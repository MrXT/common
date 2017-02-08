
package com.project.common.bean;


/**
 * 定义返回的json格式定义
 * ClassName: ResponseJson <br/>
 * Package Name:com.karakal.interceptor
 * @author xt(di.xiao@karakal.com.cn)
 * @version 1.0
 * Date:2016年7月28日上午11:58:22
 * Copyright (c) 2016, manzz.com All Rights Reserved.
 */
public class ResponseJson {
    private Integer status ; // 返回消息
    private String msg ; // 返回状态码
    private Object data;//数据
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    
    public Object getData() {
        return data;
    }

    
    public void setData(Object data) {
        this.data = data;
    }
    
}

