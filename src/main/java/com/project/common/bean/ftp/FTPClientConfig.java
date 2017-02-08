/**
 * Project Name:dtdp
 * File Name:t.java
 * Package Name:com.manzz.framework.core.util
 * Date:2014-12-01下午6:41:46
 * Copyright (c) 2014, manzz.com All Rights Reserved.
 *
 */

package com.project.common.bean.ftp;

import org.apache.commons.net.ftp.FTP;

/**
 * FTPClient配置模型
 * @author XT
 * @version 2014-12
 */
public class FTPClientConfig {
    
    private String host;
    private String username;
    private String password;
    private int    port = FTP.DEFAULT_PORT;
    private boolean binaryTransfer = true;
    private boolean passiveMode    = true;
    private String  encoding       = "UTF-8";
    private int     clientTimeout  = 30 * 1000;//30秒
    private String  workingDirectory = "/";//默认根目录

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBinaryTransfer() {
        return binaryTransfer;
    }

    public void setBinaryTransfer(boolean binaryTransfer) {
        this.binaryTransfer = binaryTransfer;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
    }
    
    public String getWorkingDirectory() {
        return workingDirectory;
    }
    
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}