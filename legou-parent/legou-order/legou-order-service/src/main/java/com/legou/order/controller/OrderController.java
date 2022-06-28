package com.legou.order.controller;

import com.lxs.legou.core.controller.BaseController;
import com.legou.order.config.TokenDecode;
import com.legou.order.service.OrderService;
import com.lxs.legou.order.po.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController<OrderService, Order> {

    private final TokenDecode tokenDecode;

    public OrderController(TokenDecode tokenDecode) {
        this.tokenDecode = tokenDecode;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Order order) throws IOException {
        //解析token获取用户名
        order.setUsername(tokenDecode.getUserInfo().get("user_name"));
        //调用service层添加订单
        service.add(order);
        return ResponseEntity.ok("下单成功");
    }


}
