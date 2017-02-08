
package com.project.common.constant;

import com.project.common.util.CommonUtils;

/**
 * 系统常量
 * ClassName: SystemConstant <br/>
 * Package Name:com.karakal.constant
 * @author xt(di.xiao@karakal.com.cn)
 * @version 1.0
 * Date:2016年7月28日下午2:27:37
 * Copyright (c) 2016, manzz.com All Rights Reserved.
 */
public interface SystemConstant {
    /**
     * 成功200
     */
    public final static int SUCCESS = 200;
    /**
     * 业务异常100
     */
    public final static int BUSINESS_FAIL = 100;
    /**
     * 服务异常101
     */
    public final static int SERVER_FAIL = 101;
    /**
     * SESSION失效 201
     */
    public final static int SESSION_INVALID = 201;
    /**
     * 没有权限 202
     */
    public final static int NO_AUTH = 202;
    /**
     * 404 请求未找到
     */
    public final static int NOT_FOUND = 404;
    /**
     * 400 坏的请求
     */
    public final static int BAD_REQUEST = 400;
    /**
     * 405 请求方式错误
     */
    public final static int METHOD_NOT_ALLOWED = 405;
    /**
     * 上传文件不能为空！
     */
    public final static String NULL_FILE = "上传文件不能为空！";
    /**
     * 必要的参数为空
     */
    public final static String NULL_MUSTPARAMETER = "必要的参数为空";
    
    /**
     * 通过ID，未查到数据
     */
    public final static String NULL_OBJECT = "通过ID未查到数据";
    
    /**
     * session用户
     */
    public static final String DEFAULT_SESSION_USER = "user";
    
    /**
     * session用户能访问的url
     */
    public static final String PASS_URLS = "passUrls";
    
    /**
     * 当前登录用户的id
     */
    public static final String DEFAULT_ID = "id";
    /**
     * 直接访问的urls
     */
    public static String[]  PERMITED_URLS = CommonUtils.readResourceArray("permited.urls");
    /**
     * 系统级的urls
     */
    public static String[]  SYSTEM_URLS = CommonUtils.readResourceArray("system.urls");
    /**
     * 个人级的urls
     */
    public static final String[] USER_URLS = CommonUtils.readResourceArray("user.urls");
    
    public static final String CHANNEL_EXPIRED = "__keyevent@0__:expired";
    public static final String REDIS_KEY_MEETING = "meeting";
    
}

