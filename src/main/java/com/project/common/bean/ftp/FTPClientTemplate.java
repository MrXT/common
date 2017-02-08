/**
 * Project Name:dtdp
 * File Name:t.java
 * Package Name:com.manzz.framework.core.util
 * Date:2014-12-01下午6:41:46
 * Copyright (c) 2014, manzz.com All Rights Reserved.
 *
 */

package com.project.common.bean.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.project.common.exception.BusinessException;
import com.project.common.exception.ServerException;


/**
 * FTP客户端工具
 * @author XT
 * @version 2014-12
 */
public class FTPClientTemplate {
    private ThreadLocal<FTPClient> ftpClientThreadLocal = new ThreadLocal<FTPClient>();
    private FTPClientConfig config;
    
    public FTPClientConfig getConfig() {
        return config;
    }
    
    public void setConfig(FTPClientConfig config) {
        this.config = config;
    }

    /**
     * 返回一个FTPClient实例
     * 
     * @throws Exception
     */
    private FTPClient getFTPClient() {
        if (ftpClientThreadLocal.get() != null && ftpClientThreadLocal.get().isConnected()) {
            return ftpClientThreadLocal.get();
        } else {
            FTPClient ftpClient = new FTPClient(); //构造一个FtpClient实例
            ftpClient.setControlEncoding(config.getEncoding()); //设置字符集
            //连接到ftp服务器
            if(connect(ftpClient)){
                //设置为passive模式，即每次数据连接之前,由client告诉server开通一个端口来传输数据避免
                if (config.isPassiveMode()) {
                    ftpClient.enterLocalPassiveMode();
                }
                setFileType(ftpClient); //设置文件传输类型
                try {
                    ftpClient.setSoTimeout(config.getClientTimeout());//设置一个命令执行后最大等待Server反馈的时间
                } catch (SocketException e) {
                    throw new ServerException("Set timeout error.", e);
                }
                ftpClientThreadLocal.set(ftpClient);
            } else {
                throw new ServerException("FTP连接失败");
            }
            return ftpClient;
        }
    }

    /**
     * 设置文件传输类型
     */
    private void setFileType(FTPClient ftpClient) {
        try {
            if (config.isBinaryTransfer()) {
                // Binary模式不会对数据进行任何处理
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            } else {
                /* 
                 * 默认为ASCII,Ascii模式会将回车换行转换为本机的回车字符
                 * 由于WINDOWS和UNIX的行结束符不一样。所以从WINDOWS用Binary传输
                 * 方式传输文本文件到UNIX时可能会出现^M。同样从UNIX用Binary方式
                 * 传输文件到WINDOWS时，也可能出现回车换行显示不正确的问题。
                 * FTP中虽然ASCII模式可以避免^M问题。
                 * 但大多数情况还是选择Binary方式，这样可以保证传输的内容不会被改变。
                 */
                ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
            }
        } catch (IOException e) {
            throw new ServerException("ftpClient.setFileType设置失败.", e);
        }
    }

    /** 连接到ftp服务器  */
    private boolean connect(FTPClient ftpClient) {
        try {
            ftpClient.connect(config.getHost(), config.getPort());

            // 连接后检测返回码来校验连接是否成功
            int reply = ftpClient.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                //登陆到ftp服务器
                if (ftpClient.login(config.getUsername(), config.getPassword())) {
                    setFileType(ftpClient);
                    return true;
                }
            } else {
                ftpClient.disconnect();
                throw new ServerException("FTP服务器拒绝连接.");
            }
        } catch (IOException e) {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect(); //断开连接
                } catch (IOException e1) {
                    throw new ServerException("断开连接失败.", e1);
                }

            }
            throw new ServerException("无法连接服务器.", e);
        }
        return false;
    }

    /** 断开ftp连接  */
    private void disconnect() {
        try {
            FTPClient ftpClient = getFTPClient();
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
                ftpClient = null;
            }
        } catch (IOException e) {
            throw new ServerException("断开连接失败.", e);
        }
    }
    
    /**
     * 在ftp服务器端创建目录（支持创建多级目录）
     * 该方法执行完后将自动关闭当前连接
     * 
     * @param pathName 文件路径
     */
    public boolean mkdir(String pathName) {
        return mkdir(pathName, config.getWorkingDirectory());
    }
    
    /**
     * 在ftp服务器端创建目录（支持创建多级目录）
     * 该方法执行完后将自动关闭当前连接
     * 
     * @param pathName 文件路径
     * @param autoClose 是否自动关闭当前连接
     */
    public boolean mkdir(String pathName, boolean autoClose) {
        return mkdir(pathName, config.getWorkingDirectory(), autoClose);
    }
    
    /**
     * 在ftp服务器端创建目录（支持创建多级目录）
     * 该方法执行完后将自动关闭当前连接
     * 
     * @param pathName 文件路径
     * @param workingDirectory
     */
    public boolean mkdir(String pathName, String workingDirectory) {
        return mkdir(pathName, workingDirectory, true);
    }
    
    /**
     * 在ftp服务器端创建目录（支持创建多级目录）
     * 
     * @param pathName 文件路径
     * @param autoClose 是否自动关闭当前连接
     */
    public boolean mkdir(String pathName, String workingDirectory, boolean autoClose) {
        try {
            FTPClient client =  getFTPClient();
            client.printWorkingDirectory();
            boolean result = createDirectory(pathName);
            if(!autoClose){
                client.changeWorkingDirectory(workingDirectory);//返回工作目录
                //System.out.println(client.printWorkingDirectory()); 
            }
            return result;
        } catch (IOException e) {
            throw new ServerException("无法创建目录."+pathName, e);
        } finally {
            if (autoClose) {
                disconnect(); //断开连接
            }
        }
    }
    
    /**
     * 可递归创建多级目录
     * @param remotePath 远程服务器文件路径(不含文件名)
     */
     private boolean createDirectory(String remotePath) {
         try{
             boolean success = true;
             FTPClient client =  getFTPClient();
             String directory = remotePath.substring(0, remotePath.lastIndexOf("/") + 1);
             // 如果远程目录不存在，则递归创建远程服务器目录
             if (!directory.equalsIgnoreCase("/") && !client.changeWorkingDirectory(directory)) {
                 int start = directory.startsWith("/") ? 1 : 0;
                 int end = directory.indexOf("/", start);
                 while (true) {
                     String subDirectory = remotePath.substring(start, end);
                     if (!client.changeWorkingDirectory(subDirectory)) {
                         if (client.makeDirectory(subDirectory)) {
                             client.changeWorkingDirectory(subDirectory);
                         } else {
                             throw new ServerException("目录创建失败.");
                         }
                     }
                     start = end + 1;
                     end = directory.indexOf("/", start);
                     // 检查所有目录是否创建完毕
                     if (end <= start) {
                         break;
                     }
                 }
             }
             return success;
         } catch (IOException e) {
             throw new ServerException("无法创建目录."+remotePath, e);
         }
     }

    /**
     * 上传一个本地文件到远程指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param localAbsoluteFile 本地文件名(包括完整路径)
     * @return 成功时，返回true，失败返回false
     */
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile) {
        return put(remoteAbsoluteFile, localAbsoluteFile, true);
    }
    
    /**
     * 上传一个本地文件到远程指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param localAbsoluteFile 本地文件名(包括完整路径)
     * @param autoClose 是否自动关闭当前连接
     * @return 成功时，返回true，失败返回false
     */
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) {
        InputStream input = null;
        try {
            // 处理传输
            input = new FileInputStream(localAbsoluteFile);
            return put(remoteAbsoluteFile, input, true);
        } catch (FileNotFoundException e) {
            throw new ServerException("没有找到本地文件."+localAbsoluteFile, e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    /**
     * 上传一个本地文件到远程指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param input 输入流
     * @param autoClose 是否自动关闭当前连接
     * @return 成功时，返回true，失败返回false
     */
    public boolean put(String remoteAbsoluteFile, InputStream input) {
        return put(remoteAbsoluteFile, input, true);
    }

    /**
     * 将输入流上传文件到远程指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param input 输入流
     * @param autoClose 是否自动关闭当前连接
     * @return 成功时，返回true，失败返回false
     */
    public boolean put(String remoteAbsoluteFile, InputStream input, boolean autoClose) {
        try {
            return getFTPClient().storeFile(remoteAbsoluteFile, input);
        } catch (IOException e) {
            throw new ServerException("无法将文件置于服务器.", e);
        } finally {
            IOUtils.closeQuietly(input);
            if (autoClose) {
                disconnect(); //断开连接
            }
        }
    }

    /**
     * 下载一个远程文件到本地的指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param localAbsoluteFile 本地文件名(包括完整路径)
     * @return 成功时，返回true，失败返回false
     */
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile) {
        return get(remoteAbsoluteFile, localAbsoluteFile, true);
    }

    /**
     * 下载一个远程文件到本地的指定文件
     * 
     * @param remoteAbsoluteFile 远程文件名(包括完整路径)
     * @param localAbsoluteFile 本地文件名(包括完整路径)
     * @param autoClose 是否自动关闭当前连接
     * @return 成功时，返回true，失败返回false
     */
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(localAbsoluteFile);
            return get(remoteAbsoluteFile, output, autoClose);
        } catch (FileNotFoundException e) {
            throw new ServerException("没有找到本地文件."+localAbsoluteFile, e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * 下载一个远程文件到指定的流 处理完后记得关闭流
     * 
     * @param remoteAbsoluteFile
     * @param output
     * @return
     */
    public boolean get(String remoteAbsoluteFile, OutputStream output) {
        return get(remoteAbsoluteFile, output, true);
    }

    /**
     * 下载一个远程文件到指定的流 处理完后记得关闭流
     * 
     * @param remoteAbsoluteFile
     * @param output
     * @param delFile
     * @return
     */
    public boolean get(String remoteAbsoluteFile, OutputStream output, boolean autoClose) {
        try {
            FTPClient ftpClient = getFTPClient();
            Boolean success = ftpClient.retrieveFile(remoteAbsoluteFile, output);
            // 处理传输
            return success;
        } catch (IOException e) {
            throw new ServerException("无法获取文件"+remoteAbsoluteFile, e);
        } finally {
            if (autoClose) {
                disconnect(); //关闭链接
            }
        }
    }

    /**
     * 从ftp服务器上删除一个文件
     * 该方法将自动关闭当前连接
     * 
     * @param delFile
     * @return
     */
    public boolean delete(String delFile) {
        return delete(delFile, true);
    }
    
    /**
     * 从ftp服务器上删除一个文件
     * 
     * @param delFile
     * @param autoClose 是否自动关闭当前连接
     * 
     * @return
     */
    public boolean delete(String delFile, boolean autoClose) {
        try {
            getFTPClient().deleteFile(delFile);
            return true;
        } catch (IOException e) {
            throw new ServerException("无法删除文件"+delFile, e);
        } finally {
            if (autoClose) {
                disconnect(); //关闭链接
            }
        }
    }
    
    /**
     * 批量删除
     * 该方法将自动关闭当前连接
     * 
     * @param delFiles
     * @return
     */
    public boolean delete(String[] delFiles) {
        return delete(delFiles, true);
    }

    /**
     * 批量删除
     * 
     * @param delFiles
     * @param autoClose 是否自动关闭当前连接
     * 
     * @return
     */
    public boolean delete(String[] delFiles, boolean autoClose) {
        try {
            FTPClient ftpClient = getFTPClient();
            for (String s : delFiles) {
                ftpClient.deleteFile(s);
            }
            return true;
        } catch (IOException e) {
            throw new ServerException("无法删除文件.", e);
        } finally {
            if (autoClose) {
                disconnect(); //关闭链接
            }
        }
    }

    /**
     * 列出远程默认目录下所有的文件
     * 
     * @return 远程默认目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
     */
    public String[] listNames() {
        return listNames(null, true);
    }
    
    /**
     * 列出远程默认目录下所有的文件
     * 
     * @param autoClose 是否自动关闭当前连接
     * @return 远程默认目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
     */
    public String[] listNames(boolean autoClose) {
        return listNames(null, autoClose);
    }

    /**
     * 列出远程目录下所有的文件
     * 
     * @param remotePath 远程目录名
     * @param autoClose 是否自动关闭当前连接
     * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
     */
    public String[] listNames(String remotePath, boolean autoClose) {
        try {
            String[] listNames = getFTPClient().listNames(remotePath);
            return listNames;
        } catch (IOException e) {
            throw new ServerException("列出远程目录下所有的文件时出现异常", e);
        } finally {
            if (autoClose) {
                disconnect(); //关闭链接
            }
        }
    }
    
    public long getFileSize(String pathName){
        FTPClient ftpClient = getFTPClient();
        FTPFile[] file;
        try {
            file = ftpClient.listFiles(pathName);
            return file[0].getSize();
        } catch (IOException e) {
            throw new ServerException("无法获取文件大小", e);
        }catch (ArrayIndexOutOfBoundsException e) {
            throw new BusinessException("无法获取到文件");
        }
    }
}