package com.lxs.legou.item.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.item.po.SpecParam;
import com.lxs.legou.item.service.ISpecParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item/param")
@CrossOrigin
public class SpecParamController extends BaseController<ISpecParamService, SpecParam> {

    @ApiOperation(value = "查询", notes = "根据实体条件查询参数")
    @PostMapping(value = "/select-param-by-entity")
    public List<SpecParam> selectSpecParamApi(@RequestBody SpecParam entity) {
        return service.list(entity);
    }


}