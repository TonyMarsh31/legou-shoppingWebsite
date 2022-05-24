package com.lxs.legou.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.admin.po.Dept;
import com.lxs.legou.admin.service.IDeptService;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl extends CrudServiceImpl<Dept> implements IDeptService {

    public List<Dept> list(Dept entity) {
        QueryWrapper<Dept> queryWrapper = Wrappers.query();
        if (null != entity.getAddress() && !"".equals(entity.getAddress().trim())) {
            queryWrapper.like("address", entity.getAddress());
        }
        if (null != entity.getTitle() && !"".equals(entity.getTitle().trim())) {
            queryWrapper.like("title", entity.getTitle());
        }
        return baseMapper.selectList(queryWrapper);
    }
}
