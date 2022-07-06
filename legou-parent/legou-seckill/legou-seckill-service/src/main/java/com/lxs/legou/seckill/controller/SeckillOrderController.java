package com.lxs.legou.seckill.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill-order")
@CrossOrigin
public class SeckillOrderController extends BaseController<ISeckillOrderService, SeckillOrder> {

    private final ISeckillOrderService orderService;

    public SeckillOrderController(ISeckillOrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/add")
    public ResponseEntity<String> add(String username, String time, Long id) {
        try {
            Boolean success = orderService.add(id, time, username);
            if (success) return ResponseEntity.ok("抢单成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("抢单失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
