/**
 * Project Name:dtdp
 * File Name:IOUtils.java
 * Package Name:com.manzz.framework.core.util
 * Date:2014-09-29下午3:49:06
 * Copyright (c) 2014, manzz.com All Rights Reserved.
 *
 */

package com.project.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.io.IOUtils;


/**
 * ClassName: IOUtils <br/>
 * Function: 输入输出流工具类. <br/>
 *
 * @author WJK
 * @version 2015-07
 */
public class IoUtils extends IOUtils{

    public static void copyTo(byte[] data, OutputStream out) {
        copyTo(new ByteArrayInputStream(data), out);
    }
    
    public static void copyTo(InputStream in,OutputStream out){
        try {
            IOUtils.copy(in, out);
        } catch (ClientAbortException e){
           //e.printStackTrace();
           //1、服务器的并发连接数超过了其承载量，服务器会将其中一些连接Down掉； 
           //2、客户关掉了浏览器，而服务器还在给客户端发送数据； (允许)
           //3、浏览器端按了Stop 
        } catch (IOException e) {
            LogUtils.ERROR(e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}

