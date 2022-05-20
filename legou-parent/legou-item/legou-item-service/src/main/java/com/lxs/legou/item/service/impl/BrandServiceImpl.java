package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.dao.BrandDao;
import com.lxs.legou.item.po.Brand;
import com.lxs.legou.item.po.Category;
import com.lxs.legou.item.service.IBrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl extends CrudServiceImpl<Brand> implements IBrandService {

    private final BrandDao dao;

    public BrandServiceImpl(BrandDao dao) {
        this.dao = dao;
    }
    // 或者不使用SpringBoot的自动注入, 而是如下所示：用mybatisPlus提供的方法获取dao
    // private final BrandDao dao = (BrandDao)getBaseMapper();

    @Override
    @Transactional()
    public boolean saveOrUpdate(Brand entity) {
        //因为有时候客户只是普通地（不涉及类别更新地）新增或修改一个实体，而最终也会执行该方法
        //所以还是要执行默认的(mybatisPlus提供)保存或更新操作
        boolean result = super.saveOrUpdate(entity);


        // --- 默认用户修改了品牌与类别的映射关系(待改善:可以增加条件判断来避免每一次都执行以下的流程) ---
        //删除商品和分类的关联
        int count = dao.deleteCategoryByBrand(entity.getId());
        //添加商品和分类的关联
        Long[] categoryIds = entity.getCategoryIds();
        if (null != categoryIds) {
            for (Long categoryId : categoryIds) {
                dao.insertCategoryAndBrand(categoryId, entity.getId());
            }
        }

        return result;
    }

    @Override
    public List<Category> selectCategoryByBrand(Long id) {
        return dao.selectCategoryByBrand(id);
    }

    @Override
    public List<Brand> selectBrandByIds(List<Long> ids) {
        QueryWrapper<Brand> queryWrapper = Wrappers.<Brand>query().in("id_", ids);
        return dao.selectList(queryWrapper);
    }
}
