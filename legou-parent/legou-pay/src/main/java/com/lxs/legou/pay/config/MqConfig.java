package com.lxs.legou.pay.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MqConfig {

    private final Environment env;

    public MqConfig(Environment env) {
        this.env = env;
    }

    /**
     * 发送订单已支付消息的交换机
     *
     * @return Exchange对象
     */
    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.directExchange(env.getProperty("mq.pay.exchange.order")).durable(true).build();
    }

    /**
     * 声明queue
     *
     * @return 订单已支付消息的队列
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(env.getProperty("mq.pay.queue.order")).build();
    }

    /**
     * 绑定queue和exchange
     *
     * @param queue    订单已支付消息的队列
     * @param exchange 发送订单已支付消息的交换机
     * @return 绑定结果
     */
    @Bean
    public Binding orderBinding(@Qualifier("orderQueue") Queue queue, @Qualifier("orderExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }
}
