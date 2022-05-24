package com.lxs.legou.admin.controller;

import com.lxs.legou.admin.po.Dept;
import com.lxs.legou.admin.service.IDeptService;
import com.lxs.legou.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController<IDeptService, Dept> {

}
