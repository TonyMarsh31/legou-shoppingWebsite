package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Spu;

public interface ISpuService extends ICrudService<Spu> {

    /**
     * 保存spu
     * @param spu
     * 其内嵌了以下数据
     *   - spu
     *   - spuDetail
     *   - skus
     */
    public void saveSpu(Spu spu);

}
