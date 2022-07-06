package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.common.utils.IdWorker;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import com.lxs.legou.seckill.task.MultiThreadingCreateOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeckillOrderServiceImpl extends CrudServiceImpl<SeckillOrder> implements ISeckillOrderService {


    private final RedisTemplate redisTemplate;
    private final SeckillGoodsDao seckillGoodsDao;
    private final IdWorker idWorker;
    private final MultiThreadingCreateOrder multiThreadingCreateOrder;

    public SeckillOrderServiceImpl(RedisTemplate redisTemplate,
                                   SeckillGoodsDao seckillGoodsDao,
                                   IdWorker idWorker,
                                   MultiThreadingCreateOrder multiThreadingCreateOrder) {
        this.redisTemplate = redisTemplate;
        this.seckillGoodsDao = seckillGoodsDao;
        this.idWorker = idWorker;
        this.multiThreadingCreateOrder = multiThreadingCreateOrder;
    }

    /**
     * 添加秒杀订单
     *
     * @param id       :商品ID
     * @param time     :商品秒杀开始时间
     * @param username :⽤户登录名
     * @return 订单是否添加成功
     */
    @Override
    public Boolean add(Long id, String time, String username) {

        // 多线程任务测试，下述方法中对休眠20s，如果多线程配置生效则不会卡20s，而是直接返回结果
        // 补充: 是Controller返回给前端的结果不需要等待20s ，但还是需要等待20s后sout结果后下单进程的操作才结束
        multiThreadingCreateOrder.createOrder();

//        //获取商品数据
//        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
//        //如果没有库存，则直接抛出异常
//        if (goods == null || goods.getStockCount() <= 0) {
//            throw new RuntimeException("已售罄!");
//        }
//        //如果有库存，则创建秒杀商品订单
//        SeckillOrder seckillOrder = new SeckillOrder();
//        seckillOrder.setId(idWorker.nextId());
//        seckillOrder.setSeckillId(id);
//        seckillOrder.setMoney(goods.getCostPrice());
//        seckillOrder.setUserId(username);
//        seckillOrder.setCreateTime(new Date());
//        seckillOrder.setStatus("0");
//        //将秒杀订单存⼊到Redis中
//        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);
//        //库存减少
//        goods.setStockCount(goods.getStockCount() - 1);
//        //判断当前商品是否还有库存
//        if (goods.getStockCount() <= 0) {
//            //并且将商品数据同步到MySQL中
//            seckillGoodsDao.updateById(goods);
//            //如果没有库存,则清空Redis缓存中该商品
//            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
//        } else {
//            //如果有库存，则将新的商品数据存入Redis中，重置旧数据
//            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, goods);
//        }
        return true;
    }
}
