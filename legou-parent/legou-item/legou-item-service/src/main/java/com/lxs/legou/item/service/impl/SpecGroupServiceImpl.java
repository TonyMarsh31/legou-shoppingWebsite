package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.dao.SpecGroupDao;
import com.lxs.legou.item.dao.SpecParamDao;
import com.lxs.legou.item.po.SpecGroup;
import com.lxs.legou.item.po.SpecParam;
import com.lxs.legou.item.service.ISpecGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupServiceImpl extends CrudServiceImpl<SpecGroup> implements ISpecGroupService {

    private final SpecParamDao specParamDao;

    public SpecGroupServiceImpl(SpecParamDao specParamDao) {
        this.specParamDao = specParamDao;
    }

    @Override
    public List<SpecGroup> list(SpecGroup entity) {
        //注意需要将BaseMapper返回的对象强制转换为SpecGroupDao类型,以使用其重写的selectList方法
        return ((SpecGroupDao) getBaseMapper()).selectList(entity);
    }

    @Override
    public void saveGroup(Long cid, List<SpecGroup> groups) {
        //根据cid删除所有的规格参数分组和规格参数项
        getBaseMapper().delete(Wrappers.<SpecGroup>query().eq("cid_", cid));
        //删除规格参数项 需要用到注入的SpecParamDao
        specParamDao.delete(Wrappers.<SpecParam>query().eq("cid_", cid));

        //添加新的规格组和规格参数项
        for (SpecGroup group : groups) {
            getBaseMapper().insert(group);
            for (SpecParam param : group.getParams()) {
                param.setGroupId(group.getId());
                specParamDao.insert(param);
            }
        }

    }
}
