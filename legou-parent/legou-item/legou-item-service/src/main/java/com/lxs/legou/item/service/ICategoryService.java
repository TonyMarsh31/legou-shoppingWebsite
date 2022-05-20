package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Category;

import java.util.List;

public interface ICategoryService extends ICrudService<Category> {
    List<String> selectNamesByIds(List<Long> ids);

}
