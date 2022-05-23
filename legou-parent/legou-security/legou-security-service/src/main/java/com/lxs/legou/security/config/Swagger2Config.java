package com.lxs.legou.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    public static final String SWAGGER_SCAN_SECURITY_PACKAGE = "com.lxs.legou.security.controller";
    public static final String SECURITY_VERSION = "1.0.0";

    @Bean
    public Docket createSecurityRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("权限管理接口")
                .apiInfo(apiSecurityInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_SECURITY_PACKAGE))//api接口包扫描路径
                .paths(PathSelectors.any())
//                .paths(PathSelectors.regex(".*/security/.*"))//可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }
    private ApiInfo apiSecurityInfo() {
        return new ApiInfoBuilder()
                .title("权限管理接口")//设置文档的标题
                .description("权限数据管理")//设置文档的描述->1.Overview
                .version(SECURITY_VERSION)//设置文档的版本信息-> 1.1 Version information
                .build();
    }

}
