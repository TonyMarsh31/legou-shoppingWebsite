package com.lxs.legou.security.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.security.po.Menu;
import org.springframework.stereotype.Service;

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
public interface IMenuService extends ICrudService<Menu> {

    /**
     * 查询用户的菜单
     * @param userId
     * @return
     */
    public List<Menu> listByUser(Long userId);

    /**
     * 查询所有菜单，选中角色已有的菜单
     * @param roleId
     * @return
     */
    public List<Menu> listChecked(Long roleId) ;

    /**
     * 关联角色和菜单
     * @param roleId
     * @param menuIds
     */
    public void doAssignMenu2Role(Long roleId, Long[] menuIds);

}
