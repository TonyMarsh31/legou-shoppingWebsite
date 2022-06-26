package com.service.auth.serviceauth;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;


public class JwtTest {
    /**
     * 使用私钥生成token
     */
    @Test
    public void testCreateJwt() throws Exception {
        //存储密钥的证书路径
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("kaikeba.jks"), "kaikeba".toCharArray());
        //密钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("kaikeba", "kaikeba".toCharArray());
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //自定义token中的payload信息
        HashMap<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("userId", "1");
        tokenMap.put("userName", "admin");
        tokenMap.put("role", "admin");
        //使用工具类,通过私钥生成token
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap),
                new RsaSigner(privateKey));
        String token = jwt.getEncoded();
        System.out.println(token);

    }

    //使用公钥解析token
    @Test
    public void testVerifyJwt() {
        //jwt的token ---> 由上一个测试类方法生成
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYWRtaW4iLCJ1c2VyTmFtZSI6ImFkbWluIiwidXNlcklkIjoiMSJ9.R9NJWKjN6j3DwSCEwQUY5M6EFWi74CWeDQbBiPbhuwLw1Cx6JRhXC3MEp-TOGoXhg8_CBwiG3XpTZy4ojQMQlPtetWQ32WPDxgqtXtNUNJA4gRvx0ULEzgQXxA_jora0kEZlOb4jTIa3MeKn0JHJYssqU-t_MgVdpgrdJNhFBkwmDJWB8rGSXaOTaBpTUl7r1Ul9uhN2lXW0lUoRQnBs6lz2Tugu-o4HEaHDHOW8DyJbfs3PHmRJ2IwX3gHkv1YJ_dr4fQsAQGtEV4fzh8NZMPsTIhQIHfTu5dK-ByxO4XhTNxS2gJx5_FzG8bn1WLUo-HCMB8sd7FDNylPbWupkGA";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwtYpjt7NtpS1B51x6PUK\n" +
                "7ryvKySK4VQi7KUCGBm6kisErNM+FwdgKMbpQxTtWoYyXfQsWwuhBW45+uF+Z5DU\n" +
                "DaLtHlMV55eA5fkGLFZ1F9ppZC+2Etsy1CyPqA0Mx8R0/HbMB1no4KTlQpqST7Jj\n" +
                "CdtwLWqUd68zDlfToIsWB1fHuYHbH/DCGUBmZb+16805/SjWkYvj3B6F+WJ8Gm47\n" +
                "/OJBH+wo7k4GWZ7OXdMcNnYWMyBfa4abjo7cxjoHL2fDanS6And4Sh3cZEJde4Wg\n" +
                "XsEktvR/EaZR7CeQzwzOg47+5cCcFSYgmVfpDyLsBnFkG3WFs/qZ3yPzy+DQKLIF\n" +
                "2wIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        //使用工具类,通过公钥解析token
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取token中的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //直接打印jwt令牌本身
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    //生成一个Admin的jwt令牌
    @Test
    public void testCreateAdminJwt() throws Exception {
        //证书文件
        String key_location = "kaikeba.jks";
        //密钥库密码
        String keystore_password = "kaikeba";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory keyStoreKeyFactory = new org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "kaikeba";
        //密钥别名
        String alias = "kaikeba";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("user_name", "admin");
        tokenMap.put("authorities", new String[] {"ROLE_ADMIN"});
        tokenMap.put("client_id", "client");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }


}