package com.legou.order.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.order.po.Order;

public interface OrderService extends ICrudService<Order> {

    /**
     * 添加订单
     * @param order 订单实体类
     */
    void add(Order order);

}
