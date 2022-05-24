package com.lxs.legou.security.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import com.lxs.legou.security.service.IRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<IRoleService, Role> {

	@Override
	public void afterEdit(Role entity) {
		//生成用户列表, 如：1,3,4
		List<User> users = service.selectUserByRole(entity.getId());
		Long[] ids = new Long[users.size()];
		for (int i=0; i< users.size(); i++) {
			ids[i] = users.get(i).getId();
		}
		entity.setUserIds(ids);
	}

}
