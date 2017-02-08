
package com.project.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * 日志工具类
 * ClassName: LogUtil <br/>
 * Package Name:com.karakal.util
 * @author xt(di.xiao@karakal.com.cn)
 * @version 1.0
 * Date:2016年7月28日下午1:51:38
 * Copyright (c) 2016, manzz.com All Rights Reserved.
 */
public class LogUtils {
    
    private static final Logger LOGGER = Logger.getLogger(LogUtils.class);
    
    public static void DEBUG(String ... args){
        if(LOGGER.isDebugEnabled()){
            StringBuffer str = new StringBuffer();
            for(String arg : args){
                str.append(arg);
            }
            LOGGER.debug(str.toString());
        }
    }
    
    public static void INFO(String ... args){
        if(LOGGER.isInfoEnabled()){
            StringBuffer str = new StringBuffer();
            for(String arg : args){
                str.append(arg);
            }
            LOGGER.info(str.toString());
        }
    }
    
    public static void WARN(String ... args){
        StringBuffer str = new StringBuffer();
        for(String arg : args){
            str.append(arg);
        }
        LOGGER.warn(str.toString());
    }
    
    public static void ERROR(String ... args){
        StringBuffer str = new StringBuffer();
        for(String arg : args){
            str.append(arg);
        }
        LOGGER.error(str.toString());
    }
    /**
     * 异常日志打印到文件，且控制台显示异常
     * @param e
     */
    public static void LOGEXCEPTION(Throwable e){
        StringWriter errorsWriter = new StringWriter();  
        e.printStackTrace(new PrintWriter(errorsWriter));  
        ERROR(errorsWriter.toString());  
    }
}
