package com.lxs.legou.admin.controller;

import com.lxs.legou.admin.po.Dict;
import com.lxs.legou.admin.service.IDictService;
import com.lxs.legou.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict")
public class DictController extends BaseController<IDictService, Dict> {


}
