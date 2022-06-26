package com.service.auth.serviceauth.interceptor;

import com.service.auth.serviceauth.utils.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 在使用feign来访问微服务前，通过拦截器添加一个请求头，
 * 该请求头的值就是AdminJWT令牌，这样就可以确保内部微服务之间的相互调用不会被鉴权模块拦截了。
 */
@Component
public class TokenRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = null;
        try {
            token = AdminToken.adminToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestTemplate.header("Authorization", "Bearer " + token);
    }
}
