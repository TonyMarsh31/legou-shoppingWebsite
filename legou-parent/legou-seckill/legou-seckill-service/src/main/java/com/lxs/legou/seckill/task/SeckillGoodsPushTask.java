package com.lxs.legou.seckill.task;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 任务：每隔30s查询数据库,推送秒杀商品
 */
@Component
public class SeckillGoodsPushTask {
    /**
     * 每隔30s查询数据库,推送秒杀商品
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis(){
        System.out.println("每隔30s查询数据库,推送秒杀商品");
    }
}
