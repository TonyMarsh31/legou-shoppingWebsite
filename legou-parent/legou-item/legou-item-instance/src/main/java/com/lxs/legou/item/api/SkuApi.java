package com.lxs.legou.item.api;

import com.lxs.legou.item.po.Sku;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/item/sku")
public interface SkuApi {

    @ApiOperation(value="查询spu对应的sku", notes="根据spuId查询sku集合")
    @GetMapping("/select-skus-by-spuid/{id}")
    List<Sku> selectSkusBySpuId(@PathVariable("id") Long spuId);

    @ApiOperation(value="加载", notes="根据ID加载")
    @GetMapping("/edit/{id}")
    public Sku edit(@PathVariable Long id);

}