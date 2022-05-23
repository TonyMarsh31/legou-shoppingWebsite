package com.lxs.legou.security.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;

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
public interface IUserService extends ICrudService<User> {

    /**
     * 根据用户id查询角色
     * @param id
     * @return
     */
    public List<Role> selectRoleByUser(Long id);

    /**
     * 根据用户名，查询用户个数
     * @param userName
     * @return
     */
    public Integer findCountByUserName(String userName);

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    public User getUserByUserName(String userName);

}
