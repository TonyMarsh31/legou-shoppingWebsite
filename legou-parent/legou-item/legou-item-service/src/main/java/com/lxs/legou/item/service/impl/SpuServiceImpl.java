package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.item.service.ISkuService;
import com.lxs.legou.item.service.ISpuDetailService;
import com.lxs.legou.item.service.ISpuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpuServiceImpl extends CrudServiceImpl<Spu> implements ISpuService {

    private final ISpuDetailService spuDetailService;

    private final ISkuService skuService;

    public SpuServiceImpl(ISpuDetailService spuDetailService, ISkuService skuService) {
        this.spuDetailService = spuDetailService;
        this.skuService = skuService;
    }

    /**
     * 用户创建好商品信息后，将前端传递过来的复杂Json数据持久化到数据库中
     * 其中包含了SPU基本数据，SPUDetail数据集，还有SKU列表
     *
     * 业务处理的逻辑是：
     *
     * 1. 持久化基本SPU数据到SPU表中 → 这一操作生成了spu_表中的主键id
     * 2. 根据spu_id （其同时作为spuDetail表中的主键)，持久化spuDetail数据
     * 3. 执行sku列表的持久化操作，因为同时也有可能是更新操作，所以业务逻辑统一为：先删除所有sku列表再insert新的数据
     * @param spu
     * 其内嵌了以下数据
     *   - spu
     *   - spuDetail
     *   - skus
     */
    @Override
    @Transactional
    public void saveSpu(Spu spu) {
        //保存spu -> spu持久化产生主键属性
        this.saveOrUpdate(spu);

        //保存spuDetail
        if (null == spu.getSpuDetail().getId()) { //若无主键数据则执行添加
            spu.getSpuDetail().setId(spu.getId());
            spuDetailService.save(spu.getSpuDetail());
        } else {
            spuDetailService.updateById(spu.getSpuDetail()); //否认执行修改(更新)
        }

        //保存skus
        //删除当前spu的所有的sku
        skuService.remove(Wrappers.<Sku>query().eq("spu_id_", spu.getId()));
        //再逐个添加新的sku
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            skuService.save(sku);
        }

    }

}
