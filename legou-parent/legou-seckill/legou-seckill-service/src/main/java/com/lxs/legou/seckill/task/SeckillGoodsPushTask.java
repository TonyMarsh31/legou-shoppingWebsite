package com.lxs.legou.seckill.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.common.utils.DateUtil;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 任务：每隔30s查询数据库,推送秒杀商品
 */
@SuppressWarnings("rawtypes")
@Component
public class SeckillGoodsPushTask {

    private final RedisTemplate redisTemplate;
    private final SeckillGoodsDao seckillGoodsDao;

    public SeckillGoodsPushTask(RedisTemplate redisTemplate,
                                SeckillGoodsDao seckillGoodsDao) {
        this.redisTemplate = redisTemplate;
        this.seckillGoodsDao = seckillGoodsDao;
    }

    /**
     * 每隔30s查询mysql数据库,推送符合条件的秒杀商品数据到Redis中
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis() {
        //1 获取当前时间以后的(包含当前区间)的5个秒杀时间间隔点
        //TODO 数据库中的date_menu_字段所表述的含义不清,其字段注释为“参与秒杀的时间段”，但其本身是一个时间点，无法明确是秒杀活动开始的时间点还是结束的时间点
        // [ 如果表示的是结束的时间点，则需要重写下一行代码中用到的DateUtil的getDateMenus方法! ]
        // DateUtil默认date_menu_字段表示的是活动开始的时间，例如现在是18:30 ，DateUtil工具类会返回 18:00,20:00……5个date对象,
        // 但若表示的是结束的时间，那么第一个18:00的相关商品数据无需做推送Redis的操作
        List<Date> dateMenus = DateUtil.getDateMenus();//
        for (Date dateMenu : dateMenus) {
            String extName = DateUtil.data2str(dateMenu, DateUtil.PATTERN_YYYYMMDDHH);//数据示例: 2020122110

            //2 根据获取到的时间间隔点,查询mysql数据库中秒杀商品列表数据，条件如下：
            /*
             select * from seckill_goods_
             where stock_count_>0
             and `status_`='1'
             and date_menu_="当前秒杀时间点"
             and id_ not in (redis中已有id)
             */
            QueryWrapper<SeckillGoods> query = Wrappers.query();
            query.gt("stock_count_", 0);
            query.eq("status_", 1);
            query.eq("date_menu_", extName);
            //获取redis中已存在的秒杀商品id
            Set keys = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).keys();
            if (keys != null && keys.size() > 0) {
                query.notIn("id_", keys); //排除已存在的秒杀商品id
            }

            //3 将查询到的秒杀商品数据推送到redis中
            // Redis存储结构: Hash结构,namespace为date_menu_字段的活动时间点
            // key: "seckill_goods_"+商品id, value: 秒杀商品对象
            List<SeckillGoods> seckillGoods = seckillGoodsDao.selectList(query);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).put(seckillGood.getId(), seckillGood);
                //todo 设置有效期,为测试方便这里没有设置有效期
            }

        }
    }
}

