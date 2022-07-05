package com.lxs.legou.seckill.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.seckill.po.SeckillGoods;

import java.util.List;

public interface ISeckillGoodsService extends ICrudService<SeckillGoods> {

    /**
     * 获取指定时间对应的秒杀商品列表
     * @param key 秒杀活动时间点，即数据库中的date_menu_字段
     */
    List<SeckillGoods> list(String key);

    /**
     * 根据秒杀商品ID查询该商品详情
     * @param time:时间区间
     * @param id:商品ID
     */
    SeckillGoods one(String time,Long id);

}
