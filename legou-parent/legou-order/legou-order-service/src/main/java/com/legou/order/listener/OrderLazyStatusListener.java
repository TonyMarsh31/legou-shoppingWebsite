package com.legou.order.listener;

import com.legou.order.service.OrderService;
import com.lxs.legou.order.po.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${mq.order.queue.dlx}")
public class OrderLazyStatusListener {

    private final OrderService orderService;

    public OrderLazyStatusListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitHandler
    public void handlerData(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            Long id = Long.parseLong(msg);
            Order order = orderService.getById(id);
            order.setOrderStatus("3");
            orderService.updateById(order);
            //todo 回滚库存（作业）
        }
    }

}
