package com.service.auth.serviceauth.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成管理员令牌,用于需要权限的微服务之间的内部调用
 * (通过Feign拦截器拦截后,通过本工具类在header中添加adminToken)
 */
public class AdminToken {

    public static String adminToken() throws IOException {
        //证书文件
        String key_location = "kaikeba.jks";
        //密钥库密码
        String keystore_password = "kaikeba";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "kaikeba";
        //密钥别名
        String alias = "kaikeba";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("user_name", "admin");
        tokenMap.put("client_id", "client");
        tokenMap.put("authorities", new String[] {"ROLE_ADMIN"});
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        return jwt.getEncoded();
    }

}