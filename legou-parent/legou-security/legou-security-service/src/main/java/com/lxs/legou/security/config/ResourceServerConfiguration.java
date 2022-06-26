package com.lxs.legou.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)//激活方法上的PreAuthorize注解
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    public ResourceServerConfiguration(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/user/**", "/security/user/**").permitAll() //允许所有用户访问，不需要认证
                .antMatchers("/user/login", "/security/user/login").permitAll() //仅开放登录接口，不需要认证
                .antMatchers("/book/**").hasRole("ADMIN") //用于测试
                .antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);
    }
}
