package com.lxs.legou.item.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.service.ISkuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController extends BaseController<ISkuService, Sku> {

    @ApiOperation(value="查询spu对应的sku", notes="根据spuId查询sku集合")
    @GetMapping("/select-skus-by-spuid/{id}")
    public List<Sku> selectSkusBySpuId(@PathVariable("id") Long spuId) {
        Sku skuTemplate = new Sku();
        skuTemplate.setSpuId(spuId);
        return service.list(skuTemplate);
    }

    /**
     * 减库存
     * @param num 数量
     * @param skuId 商品id
     */
    @PostMapping("/decr-count")
    public void decrCount(@RequestParam("num") Integer num, @RequestParam("skuId") Long skuId) {
        service.decrCount(num, skuId);
    }

}
