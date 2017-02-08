package com.project.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import com.project.common.bean.BaseEntity;
import com.project.common.bean.ftp.FTPClientConfig;
import com.project.common.bean.ftp.FTPClientTemplate;
import com.project.common.bean.mongodb.MongoDao;
import com.project.common.constant.SystemConstant;
import com.project.common.spring.interceptor.PathFilter;

/**
 * 业务相关配置类 ClassName: BusinessConfig <br/>
 * Package Name:com.karakal.config
 * @version 1.0 Date:2016年7月28日下午2:00:36 Copyright (c) 2016, manzz.com All
 *          Rights Reserved.
 */
@Configuration
public class BusinessConfig {
    @Value("${pageSize}")
    private Integer pageSize;
    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private int ftpPort;
    @Value("${ftp.username}")
    private String ftpUserName;
    @Value("${ftp.password}")
    private String ftpPassword;
    @Bean(name = "statusMap")
    public Map<Integer, String> statusMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        BaseEntity.ROWS = pageSize;
        map.put(SystemConstant.BAD_REQUEST, "BAD_REQUEST,请求参数错误");
        map.put(SystemConstant.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED,请求方法错误");
        map.put(SystemConstant.NOT_FOUND, "NOT_FOUND,请求地址没找到");
        map.put(SystemConstant.SESSION_INVALID, "SESSION失效！");
        return map;
    }
    /**
     * 添加打印请求地址
     * @return
     */
    @Bean
    public FilterRegistrationBean basicFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new PathFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
    /**
     * 重写spring boot的error接口
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
                container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
                container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405"));
                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                container.setSessionTimeout(30*60);//单位s
            }

        };
    }
    /**
     * ftp注入
     * @return
     */
    @Bean
    public FTPClientTemplate ftpClient(){
        FTPClientTemplate clientTemplate = new FTPClientTemplate();
        FTPClientConfig config = new FTPClientConfig();
        config.setHost(ftpHost);
        config.setPort(ftpPort);
        config.setUsername(ftpUserName);
        config.setPassword(ftpPassword);
        clientTemplate.setConfig(config);
        return clientTemplate;
    }
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * mongodb注入
     * @return
     */
    @Bean
    public MongoDao mongoDao() {
        MongoDao mongoDao = new MongoDao();
        mongoDao.setMongoTemplate(mongoTemplate);
        return mongoDao;
    }
}
