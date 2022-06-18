package com.service.auth.serviceauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

    final
    AuthenticationManager authenticationManager;//

    private final DataSource dataSource;//配置文件配置的数据库信息

    public AuthorizationServerConfiguration(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        //使用JWT存储token
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        //使用非对称加密算法生成一个密钥，用于生成token
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("kaikeba.jks"), "kaikeba".toCharArray());   //证书路径和 获取密钥库
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("kaikeba"));  //获取密钥库中的密钥 密钥名称
        return converter;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        //使用jdbc存储client信息
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //允许表单认证
        security.allowFormAuthenticationForClients()
                // 开启/oauth/token_key验证端口允许任意权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口允许任意权限访问
                .checkTokenAccess("permitAll()");
    }

    @Override
    //配置客户端详情服务(clientDetailsService)，主要配置客户端的详细信息
    //你能够把客户端详情信息直接写死在这里，或者是通过数据库来存储调度详情信息
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //直接读取数据库，需要保证数据库中有客户端信息 (oauth_client_details),否则资源服务器无法获取认证数据
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    //配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services),还有token存储方式(tokenStore)
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter()).authenticationManager(authenticationManager);

        //配置tokenService参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(false); //不支持刷新token
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); //30天有效
        endpoints.tokenServices(tokenServices);

    }
}
