package com.lxs.legou.item.api;

import com.lxs.legou.item.po.Category;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/item/category")
public interface CategoryApi {

    @ApiOperation(value="查询", notes="根据实体条件查询")
    @RequestMapping(value = "/list")
    List<Category> list(Category category);

}
