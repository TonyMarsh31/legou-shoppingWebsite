package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import com.lxs.legou.seckill.task.MultiThreadingCreateOrder;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderServiceImpl extends CrudServiceImpl<SeckillOrder> implements ISeckillOrderService {

    private final MultiThreadingCreateOrder multiThreadingCreateOrder;

    public SeckillOrderServiceImpl(MultiThreadingCreateOrder multiThreadingCreateOrder) {
        this.multiThreadingCreateOrder = multiThreadingCreateOrder;
    }


    @Override
    public Boolean add(Long id, String time, String username) {

        multiThreadingCreateOrder.createOrder(id,time,username);
        return true;
    }
}
