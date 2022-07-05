package com.lxs.legou.seckill.controller;

import com.lxs.legou.common.utils.DateUtil;
import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckill-goods")
@CrossOrigin
public class SeckillGoodsController extends BaseController<ISeckillGoodsService, SeckillGoods> {

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

}
