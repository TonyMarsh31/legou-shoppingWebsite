package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.pojo.SeckillStatus;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import com.lxs.legou.seckill.task.MultiThreadingCreateOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeckillOrderServiceImpl extends CrudServiceImpl<SeckillOrder> implements ISeckillOrderService {

    private final MultiThreadingCreateOrder multiThreadingCreateOrder;
    private final RedisTemplate redisTemplate;

    public SeckillOrderServiceImpl(MultiThreadingCreateOrder multiThreadingCreateOrder, RedisTemplate redisTemplate) {
        this.multiThreadingCreateOrder = multiThreadingCreateOrder;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Boolean add(Long id, String time, String username) {

        //新建一个秒杀订单队列信息用于测试
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);

        //todo 以下操作可以在OpenResty中使用Lua脚本实现以提高性能
        //将秒杀排队信息，leftPush存入redis的list队列中，利用其左进右出的数据结构特性实现秒杀的排队
        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);

        //多线程抢单
        multiThreadingCreateOrder.createOrder();
        return true;
    }
}
