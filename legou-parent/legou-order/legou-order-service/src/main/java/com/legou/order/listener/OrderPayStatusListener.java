package com.legou.order.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderPayStatusListener {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public OrderPayStatusListener(ObjectMapper objectMapper, OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @RabbitHandler
    public void handlerData(String msg) {
        //反序列化消息数据
        Map<String, String> map = null;
        try {
            map = objectMapper.readValue(msg, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //更新订单状态
        if (map != null) {
            if (map.get("trade_status").equals("TRADE_SUCCESS")) {
                orderService.updatePayStatus(map.get("out_trade_no"), map.get("trade_no"));
            } else {
                //todo 删除订单，支付失败，回滚库存（作业）
            }
        }
    }

}
