package com.lxs.legou.item.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.item.po.SpecParam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SpecParamDao extends ICrudDao<SpecParam> {

    @Select("select * from spec_param_ where group_id_ = #{groupId}")
    public List<SpecParam> findByGroupId(Integer groupId);

}