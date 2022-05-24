package com.lxs.legou.security.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;

import java.util.List;


public interface IRoleService extends ICrudService<Role> {

    /**
     * 查询角色的用户
     * @param id
     * @return
     */
    List<User> selectUserByRole(Long id);

}
