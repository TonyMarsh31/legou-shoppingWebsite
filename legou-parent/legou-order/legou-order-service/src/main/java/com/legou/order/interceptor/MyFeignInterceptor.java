package com.legou.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 购物车微服务中调用的是商品微服务legou-item，其开启了Oauth2鉴权
 * 所以需要把购物车中的JWT令牌加到feign的请求头上，再传递给商品微服务，
 * 防止商品微服务获取不到JWT令牌导致无法调用商品微服务的接口
 */
@Component
public class MyFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获得当前请求头，传递给商品微服务
        //注意！
        //此处获取的是当前线程的request信息，如果使用hystrix线程隔离，则需要采用信号量隔离方案，
        //即需要在hystrix配置中添加strategy: SEMAPHORE，否则报错
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            //获取请求头，向下传
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            //往往有多个请求头，所以需要遍历
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement(); //头名
                String value = request.getHeader(name); //头值
                System.out.println(name + " -----> " + value);
                //把请求头传递给feign
                requestTemplate.header(name, value);
            }
        }
    }
}