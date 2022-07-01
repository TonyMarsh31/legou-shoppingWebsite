package com.lxs.legou.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    //创建支付宝所需要的对象
    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(AlipayUtils.gatewayUrl, AlipayUtils.app_id, AlipayUtils.merchant_private_key,"json", AlipayUtils.charset,
                AlipayUtils.alipay_public_key, AlipayUtils.sign_type);
    }

    @Bean    //支付信息的配置
    public AlipayTradePagePayRequest  alipayTradePagePayRequest(){
        return  new AlipayTradePagePayRequest();
    }


}
