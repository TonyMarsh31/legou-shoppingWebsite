package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillGoodsServiceImpl extends CrudServiceImpl<SeckillGoods> implements ISeckillGoodsService {

    private final RedisTemplate redisTemplate;

    public SeckillGoodsServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param key 秒杀时区 (即数据库中的date_menu_字段)
     * @return 秒杀商品列表
     */
    @Override
    public List list(String key) {
        return redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + key).values();
    }

}
