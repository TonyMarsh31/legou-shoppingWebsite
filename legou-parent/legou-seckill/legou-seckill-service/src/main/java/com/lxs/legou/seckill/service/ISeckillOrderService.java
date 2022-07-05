package com.lxs.legou.seckill.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.seckill.po.SeckillOrder;

public interface ISeckillOrderService extends ICrudService<SeckillOrder> {

    /**
     * 添加秒杀订单
     * @param id:商品ID
     * @param time:商品秒杀开始时间
     * @param username:⽤户登录名
     * @return 订单是否添加成功
     */
    Boolean add(Long id, String time, String username);


}
