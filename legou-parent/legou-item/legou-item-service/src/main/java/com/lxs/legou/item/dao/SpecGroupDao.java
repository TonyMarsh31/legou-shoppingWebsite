package com.lxs.legou.item.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.item.po.SpecGroup;

import java.util.List;

public interface SpecGroupDao extends ICrudDao<SpecGroup> {

    /**
     * 根据实体类的条件生成动态sql语句查询规格分组
     * @param specGroup
     * @return
     */
    List<SpecGroup> selectList(SpecGroup specGroup);

}
