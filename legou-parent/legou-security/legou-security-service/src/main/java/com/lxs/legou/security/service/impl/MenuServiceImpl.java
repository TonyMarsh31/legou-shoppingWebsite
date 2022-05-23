package com.lxs.legou.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.po.BaseTreeEntity;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.security.dao.MenuDao;
import com.lxs.legou.security.po.Menu;
import com.lxs.legou.security.service.IMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MenuServiceImpl extends CrudServiceImpl<Menu> implements IMenuService {

	public List<Menu> listByUser(Long userId) {
		return ((MenuDao) getBaseMapper()).selectByUserId(userId);
	}

	public List<Menu> listChecked(Long roleId) {
		List<Menu> menus = this.list(); //查询所用
		List<Menu> roleMenus = ((MenuDao) getBaseMapper()).selectByRoleId(roleId);
		updateChecked(menus, roleMenus);
		return menus;
	}

	private <T extends BaseTreeEntity> void updateChecked(List<T> menus, List<Menu> roleMenus) {
		for (BaseTreeEntity m1 : menus) {
			for (Menu m2 : roleMenus) {
				if (m1.getId().equals(m2.getId())) {
					((Menu) m1).setSelected(true);
				}
			}
		}
	}

	@Transactional()
	public void doAssignMenu2Role(Long roleId, Long[] menuIds) {
		MenuDao dao = ((MenuDao) getBaseMapper());
		dao.deleteMenuByRole(roleId);
		for (Long menuId : menuIds) {
			dao.insertMenuAndRole(menuId, roleId);
		}
	}

	@Override
	public List<Menu> list(Menu entity) {
		QueryWrapper<Menu> queryWrapper = Wrappers.query();
		if (StringUtils.isNotEmpty(entity.getName())) {
			queryWrapper.like("name", entity.getName());
		}
		if (null != entity.getTitle() && !"".equals(entity.getTitle().trim())) {
			queryWrapper.like("title", entity.getTitle());
		}
		return baseMapper.selectList(queryWrapper);
	}
}
