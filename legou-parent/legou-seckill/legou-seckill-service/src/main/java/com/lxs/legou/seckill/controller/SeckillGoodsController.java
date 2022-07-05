package com.lxs.legou.seckill.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill-goods")
@CrossOrigin
public class SeckillGoodsController extends BaseController<ISeckillGoodsService, SeckillGoods> {



}
