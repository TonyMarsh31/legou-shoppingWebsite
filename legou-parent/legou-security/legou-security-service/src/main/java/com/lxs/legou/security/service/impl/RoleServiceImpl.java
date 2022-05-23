package com.lxs.legou.security.service.impl;

import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.security.dao.RoleDao;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import com.lxs.legou.security.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title:
 * @Description: 
 *
 * @Copyright 2019 lxs - Powered By 雪松
 * @Author: lxs
 * @Date:  2019/10/9
 * @Version V1.0
 */
@Service
public class RoleServiceImpl extends CrudServiceImpl<Role> implements IRoleService {

	@Override
	public List<User> selectUserByRole(Long id) {
		return ((RoleDao) getBaseMapper()).selectUserByRole(id);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean saveOrUpdate(Role entity) {
		RoleDao dao = ((RoleDao) getBaseMapper());
		boolean result = super.saveOrUpdate(entity);

		dao.deleteUserByRole(entity.getId()); //删除角色的用户

		//添加角色和权限关系
		Role role = (Role) entity;

		//添加用户和角色关系
		if (null != role.getUserIds()) {
			for (Long userId : role.getUserIds()) {
				dao.insertUserAndRole(userId, entity.getId());
			}
		}
		return result;
	}

}
