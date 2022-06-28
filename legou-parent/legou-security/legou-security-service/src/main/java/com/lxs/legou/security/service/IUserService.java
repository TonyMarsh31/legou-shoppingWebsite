package com.lxs.legou.security.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;

import java.util.List;


public interface IUserService extends ICrudService<User> {

    /**
     * 根据用户id查询角色
     * @param id 用户id
     * @return 角色列表
     */
    List<Role> selectRoleByUser(Long id);

    /**
     * 根据用户名，查询用户个数
     * @param userName 用户名
     * @return 用户个数
     */
    Integer findCountByUserName(String userName);

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户
     */
    User getUserByUserName(String userName);

    /**
     * 增加会员积分
     * @param point 积分
     * @param username 用户名
     */
    void addPoint(Long point, String username);

}
