package com.lxs.legou.seckill.task;

import com.lxs.legou.common.utils.IdWorker;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.pojo.SeckillStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {


    private final RedisTemplate redisTemplate;
    private final SeckillGoodsDao seckillGoodsDao;
    private final IdWorker idWorker;

    public MultiThreadingCreateOrder(RedisTemplate redisTemplate, SeckillGoodsDao seckillGoodsDao, IdWorker idWorker) {
        this.redisTemplate = redisTemplate;
        this.seckillGoodsDao = seckillGoodsDao;
        this.idWorker = idWorker;
    }

    /**
     * 多线程抢单:添加秒杀订单
     */
    @Async
    public void createOrder() {

        //加载Redis秒杀订单队列中的订单数据
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();
        if (seckillStatus == null) {
            throw new RuntimeException("获取订单数据失败!");
        }
        String username = seckillStatus.getUsername(); //用户名
        Long id = seckillStatus.getGoodsId(); //商品id
        String time = seckillStatus.getTime(); //秒杀区间

        //根据id获取秒杀商品的详细信息
        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX).get(id);

        //如果没有库存，则直接抛出异常
        if (goods == null || goods.getStockCount() <= 0) {
            throw new RuntimeException("已售罄!");
        }
        //如果有库存，则创建秒杀商品订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(goods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");
        //将秒杀订单存⼊到Redis中
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);
        //库存减少
        goods.setStockCount(goods.getStockCount() - 1);
        //判断当前商品是否还有库存
        if (goods.getStockCount() <= 0) {
            //并且将商品数据同步到MySQL中
            seckillGoodsDao.updateById(goods);
            //如果没有库存,则清空Redis缓存中该商品
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
        } else {
            //如果有库存，则将新的商品数据存入Redis中，重置旧数据
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, goods);
        }

        //为方便进行多线程测试，下面模拟耗时操作
        try {
            System.out.println("*******开始模拟耗时操作********");
            Thread.sleep(10000);
            System.out.println("*******结束模拟耗时操作********");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

