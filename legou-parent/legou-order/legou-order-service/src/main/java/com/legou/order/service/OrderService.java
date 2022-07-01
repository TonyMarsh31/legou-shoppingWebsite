package com.legou.order.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.order.po.Order;

public interface OrderService extends ICrudService<Order> {

    /**
     * 添加订单
     * @param order 订单实体类
     */
    void add(Order order);


    /**
     * 修改订单完成支付状态
     * @param outTradeNo 订单编号
     * @param tradeNo 支付编号
     */
    void updatePayStatus(String outTradeNo, String tradeNo);

}
