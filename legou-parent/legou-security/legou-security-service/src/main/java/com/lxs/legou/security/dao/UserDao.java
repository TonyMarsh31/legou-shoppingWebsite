package com.lxs.legou.security.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserDao extends ICrudDao<User> {

    /**
     * 删除用户角色关联
     *
     * @param id userId
     * @return affectedNumber
     */
    int deleteRoleByUser(Long id);

    /**
     * 关联用户角色
     *
     * @param roleId roleId
     * @param userId userId
     * @return affectedNumber
     */
    int insertRoleAndUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

    /**
     * 查询用户的角色
     *
     * @param id userId
     * @return 该用户所属角色列表
     */
    List<Role> selectRoleByUser(Long id);

    /**
     * 为指定用户增加指定积分
     * @param point 积分
     * @param userName 用户名
     * @return affectedNumber
     */
    @Update(value="update user_ set point_ = point_ + #{point} where user_name_ = #{userName}")
    int addPoint(@Param(value="point") Long point , @Param(value="userName") String userName);


}
