package com.project.common.spring.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.common.bean.ResponseJson;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.LogUtils;
import com.project.common.util.ValidateUtils;

/**
 * controller层的aop，主要用来封装返回的json数据格式 ClassName: ControllerAOP <br/>
 * Package Name:com.karakal.interceptor
 * @author xt(di.xiao@karakal.com.cn)
 * @version 1.0 Date:2016年7月28日上午11:58:22 Copyright (c) 2016, manzz.com All
 *          Rights Reserved.
 */
@Aspect
@Configuration
public class ControllerAOP {

    // 定义切点Pointcut
    @Pointcut("execution(* com.project..controller.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method method = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        Date beforeDate = new Date();
        if (target.getClass().isAnnotationPresent(RestController.class) || method.isAnnotationPresent(ResponseBody.class)) {
            ResponseJson json = new ResponseJson();
            try {
                Object result = pjp.proceed();
                if (result instanceof ResponseJson) {
                    return result;
                } else {
                    json.setData(result);
                }
                json.setStatus(SystemConstant.SUCCESS);
                json.setMsg(SUCCESS);
            } catch (BusinessException e) {
                LogUtils.LOGEXCEPTION(e);
                if (ValidateUtils.isNotBlank(e.getMessage())) {
                    json.setMsg(e.getMessage());
                } else {
                    json.setMsg(BUSINESS_FAIL);
                }
                if (e.getErrorCode() != null) {
                    json.setStatus(e.getErrorCode());
                } else {
                    json.setStatus(SystemConstant.BUSINESS_FAIL);
                }
            } catch (Error | Exception e) {
                LogUtils.LOGEXCEPTION(e);
                if (ValidateUtils.isNotBlank(e.getMessage())) {
                    json.setMsg(e.getMessage());
                } else {
                    json.setMsg(SERVER_FAIL);
                }
                json.setStatus(SystemConstant.SERVER_FAIL);
            }
            LogUtils.DEBUG("请求耗时(ms)：" + (new Date().getTime() - beforeDate.getTime()));
            return json;
        } else {
            Object object = null;
            try {
                object = pjp.proceed();
            } catch (Exception e) {
                LogUtils.LOGEXCEPTION(e);
                SessionHolder.getRequest().getRequestDispatcher("/error/500?message=" + e.getMessage()).forward(SessionHolder.getRequest(), SessionHolder.getResponse());;
            }
            LogUtils.DEBUG("请求耗时(ms)：" + (new Date().getTime() - beforeDate.getTime()));
            return object;
        }

    }

    private final static String SUCCESS = "操作成功!";

    private final static String SERVER_FAIL = "服务异常!";

    private final static String BUSINESS_FAIL = "业务异常!";

}
