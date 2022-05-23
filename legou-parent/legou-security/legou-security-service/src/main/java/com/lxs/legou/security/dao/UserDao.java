package com.lxs.legou.security.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import org.apache.ibatis.annotations.Param;

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
public interface UserDao extends ICrudDao<User> {

	/**
	 * 删除用户角色关联
	 * @param id
	 * @return
	 */
	public int deleteRoleByUser(Long id);

	/**
	 * 关联用户角色
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public int insertRoleAndUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

	/**
	 * 查询用户的角色
	 * @param id
	 * @return
	 */
	public List<Role> selectRoleByUser(Long id);

}
