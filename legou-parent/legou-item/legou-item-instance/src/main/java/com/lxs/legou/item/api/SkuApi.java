package com.lxs.legou.item.api;

import com.lxs.legou.item.po.Sku;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/sku")
public interface SkuApi {

    @ApiOperation(value = "查询spu对应的sku", notes = "根据spuId查询sku集合")
    @GetMapping("/select-skus-by-spuid/{id}")
    List<Sku> selectSkusBySpuId(@PathVariable("id") Long spuId);

    @ApiOperation(value = "加载", notes = "根据ID加载")
    @GetMapping("/edit/{id}")
    Sku edit(@PathVariable Long id);

    /**
     * 减库存
     *
     * @param num   数量
     * @param skuId 商品id
     */
    @PostMapping("/decr-count")
    void decrCount(@RequestParam("num") Integer num, @RequestParam("skuId") Long skuId);


}