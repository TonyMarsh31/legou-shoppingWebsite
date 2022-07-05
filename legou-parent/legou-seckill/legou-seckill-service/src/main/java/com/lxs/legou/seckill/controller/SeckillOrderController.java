package com.lxs.legou.seckill.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill-order")
@CrossOrigin
public class SeckillOrderController extends BaseController<ISeckillOrderService, SeckillOrder> {


}
