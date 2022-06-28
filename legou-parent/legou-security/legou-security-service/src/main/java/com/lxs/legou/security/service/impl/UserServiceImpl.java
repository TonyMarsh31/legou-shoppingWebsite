package com.lxs.legou.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.security.dao.UserDao;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import com.lxs.legou.security.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends CrudServiceImpl<User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Role> selectRoleByUser(Long id) {
        return ((UserDao) getBaseMapper()).selectRoleByUser(id);
    }

    @Override
    @Transactional()
    public boolean saveOrUpdate(User entity) {
        //添加时，设置lock=false
        if (null == entity.getId()) {
            entity.setLock(false);
        }

        //对明文密码进行加密处理
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
//		passwordHelper.encryptPassword(entity); //加密md5(md5(password,salt))

        boolean result = super.saveOrUpdate(entity);

        ((UserDao) getBaseMapper()).deleteRoleByUser(entity.getId());

        Long[] roleIds = entity.getRoleIds();
        if (null != roleIds) {
            for (Long roleId : roleIds) {
                ((UserDao) getBaseMapper()).insertRoleAndUser(roleId, entity.getId());
            }
        }
        return result;

    }

    @Override
    public Integer findCountByUserName(String userName) {
        return getBaseMapper().selectCount(Wrappers.<User>query().eq("user_name_", userName));
    }

    @Override
    public User getUserByUserName(String userName) {
        return getBaseMapper().selectOne(Wrappers.<User>query().eq("user_name_", userName));
    }


    /**
     * 增加会员积分
     *
     * @param point    积分
     * @param username 用户名
     */
    @Override
    public void addPoint(Long point, String username) {
        ((UserDao) getBaseMapper()).addPoint(point, username);
    }


}
