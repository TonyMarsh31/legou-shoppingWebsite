package com.lxs.legou.security.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.security.po.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前端没有用到Menu部分的业务逻辑,但后端给予了API的实现
 */
public interface MenuDao extends ICrudDao<Menu> {

    /**
     * 根据角色查询菜单
     * @param roleId roleId
     * @return 角色所属菜单列表
     */
    List<Menu> selectByRoleId(Long roleId);

    /**
     * 根据用户查询菜单
     * @param userId userId
     * @return 用户有权访问的菜单列表
     */
    List<Menu> selectByUserId(Long userId);

    /**
     * 删除角色的菜单
     * @param id RoleId
     * @return affectedNumber
     */
    int deleteMenuByRole(Long id);

    /**
     * 关联菜单和角色
     * @param menuId menuId
     * @param roleId roleId
     * @return affectedNumber
     */
    int insertMenuAndRole(@Param("menuId") Long menuId, @Param("roleId") Long roleId);

}
