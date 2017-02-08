package com.project.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by Daniel on 16/10/2016.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket merchantStoreApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("接口文档",//大标题
                "接口文档的详细说明",//小标题
                "0.1",//版本
                "NO terms of service",
                "mr.xiao@qq.com",//作者
                "The Apache License, Version 2.0",//链接显示文字
                "www.baidu.com"//网站链接
        );

        return apiInfo;
    }

}
