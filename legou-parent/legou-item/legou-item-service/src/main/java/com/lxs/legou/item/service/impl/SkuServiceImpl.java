package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.dao.SkuDao;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.service.ISkuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl extends CrudServiceImpl<Sku> implements ISkuService {

    @Override
    public List<Sku> list(Sku entity) {
        QueryWrapper<Sku> queryWrapper = Wrappers.query();
        if (entity.getSpuId() != null) {
            queryWrapper.eq("spu_id_", entity.getSpuId());
        }
        return getBaseMapper().selectList(queryWrapper);
    }



    @Override
    public void decrCount(Integer num, Long skuId) {
        // 如下使用java代码进行库存数量对比，如果不用锁，那么很有可能出现库存为负数的超卖情况 (经典的多线程超卖问题)
        // Sku sku = getBaseMapper().selectById(skuId);
        // if (sku.getStock() >= num) {
        //     sku.setStock(sku.getStock() - num);
        //     getBaseMapper().updateById(sku);
        // }

        //所以这里使用mybatis-plus内部的乐观锁技术来解决这个问题
        //@Update(value="update sku_ set stock_ = stock_ - #{num} where id_ =#{skuId} and stock_ >= #{num}")
        ((SkuDao) getBaseMapper()).decrCount(num, skuId);
    }
}
