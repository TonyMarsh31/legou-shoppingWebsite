package com.legou.order.service.impl;

import com.legou.order.client.SkuClient;
import com.legou.order.client.SpuClient;
import com.legou.order.service.CartService;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.order.po.OrderItem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final SkuClient skuClient;
    private final SpuClient spuClient;
    private final RedisTemplate redisTemplate;

    public CartServiceImpl(@Qualifier("com.legou.order.client.SkuClient") SkuClient skuClient,
                           @Qualifier("com.legou.order.client.SpuClient") SpuClient spuClient,
                           RedisTemplate redisTemplate) {
        this.skuClient = skuClient;
        this.spuClient = spuClient;
        this.redisTemplate = redisTemplate;
    }



    /**
     * 添加购物车
     * @param id spuID
     * @param num 购买数量
     * @param username 用户名，从登录令牌中获得
     */
    @Override
    public void add(Long id, Integer num, String username) {

        if (num <= 0) {
            //删除购物车数据
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
            return;
        }

        //根据sku的Id得到sku
        Sku data = skuClient.edit(id);
        if (data != null) {
            //得到spu
            Long spuId = data.getSpuId();
            Spu spu = spuClient.edit(spuId);

            //将数据封装到OrderItem中
            //3.将数据存储到 购物车对象(order_item)中
            OrderItem orderItem = new OrderItem();

            orderItem.setCategoryId1(spu.getCid1());
            orderItem.setCategoryId2(spu.getCid2());
            orderItem.setCategoryId3(spu.getCid3());
            orderItem.setSpuId(spu.getId());
            orderItem.setSkuId(id);
            orderItem.setName(data.getTitle());//商品的名称  sku的名称
            orderItem.setPrice(data.getPrice());//sku的单价
            orderItem.setNum(num);//购买的数量
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());//单价* 数量
            orderItem.setImage(data.getImages());//商品的图片

            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
        }

    }

    @Override
    public List<OrderItem> list(String username) {
        return redisTemplate.boundHashOps("Cart_" + username).values();
    }


}
