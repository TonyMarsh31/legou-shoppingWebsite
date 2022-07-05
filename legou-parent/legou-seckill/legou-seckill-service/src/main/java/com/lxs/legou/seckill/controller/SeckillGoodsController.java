package com.lxs.legou.seckill.controller;

import com.lxs.legou.common.utils.DateUtil;
import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckill-goods")
@CrossOrigin
public class SeckillGoodsController extends BaseController<ISeckillGoodsService, SeckillGoods> {

    private final ISeckillGoodsService goodsService;

    public SeckillGoodsController(ISeckillGoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 获取当前的时间基准的5个时间段
     *
     * @return 以当前时间为基准的5个秒杀时间段
     */
    @GetMapping("/menus")
    public List<Date> dateMenus() {
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date date : dateMenus) {
            System.out.println(date);
        }
        return dateMenus;
    }

    /**
     * 根据秒杀活动时间段查询商品信息
     *
     * @param time 秒杀活动时间点，需要是符合yyyyMMddHH格式的整点时间,例如2022070518
     * @return List<SeckillGoods>
     */
    @RequestMapping("/list/{time}")
    public List<SeckillGoods> list(@PathVariable("time") String time) {
        return goodsService.list(time);
    }
}
