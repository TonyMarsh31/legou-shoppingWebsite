package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Sku;

public interface ISkuService extends ICrudService<Sku> {

    /**
     * 减库存
     * @param num 减少的数量
     * @param skuId 商品id
     */
    void decrCount(Integer num, Long skuId);


}
