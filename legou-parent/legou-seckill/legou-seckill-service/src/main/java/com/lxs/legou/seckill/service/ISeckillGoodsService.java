package com.lxs.legou.seckill.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.seckill.po.SeckillGoods;

import java.util.List;

public interface ISeckillGoodsService extends ICrudService<SeckillGoods> {

    /***
     * 获取指定时间对应的秒杀商品列表
     * @param key
     */
    List<SeckillGoods> list(String key);

}
