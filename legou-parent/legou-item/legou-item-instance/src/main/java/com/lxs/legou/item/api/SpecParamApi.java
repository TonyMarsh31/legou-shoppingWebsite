package com.lxs.legou.item.api;

import com.lxs.legou.item.po.SpecParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/param")
public interface SpecParamApi {

    @ApiOperation(value="查询", notes="根据实体条件查询参数")
    @PostMapping(value = "/select-param-by-entity", consumes = "application/json")
    List<SpecParam> selectSpecParamApi(@RequestBody SpecParam entity);


}