package com.legou.order.service;

import com.lxs.legou.order.po.OrderItem;

import java.util.List;

public interface CartService {

    /**
     * 添加购物车
     * @param id spuID
     * @param num 购买数量
     * @param username 用户名，从登录令牌中获得
     */
    void add(Long id, Integer num, String username);

    /**
     * 从redis中查询当前用户对应的购物车数据
     * @param username
     * @return
     */
    List<OrderItem> list(String username);

}
