package com.service.auth.serviceauth.service.impl;

import com.lxs.legou.security.po.Role;
import com.service.auth.serviceauth.client.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//自定义UserDetailService
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    final
    UserClient userClient;

    final
    PasswordEncoder passwordEncoder;//BCryptPasswordEncoder

    public UserDetailServiceImpl(@Qualifier("com.service.auth.serviceauth.client.UserClient") UserClient userClient,
                                 PasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //注意：这里的User是项目中的自定义类，不是spring security的类
        com.lxs.legou.security.po.User user = userClient.getByUserName(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            logger.debug("current user = " + user);
            //获取用户的授权信息
            List<Role> roles = userClient.selectRolesByUserId(user.getId());
            //声明授权文件
            for (Role role : roles) {
                if (role != null && role.getName() != null) {
                    //spring Security中权限名称必须满足ROLE_XXX,所以此处加上ROLE_前缀
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
        }
        logger.debug("granted authorities = " + grantedAuthorities);
        if (user == null) {
            return null;
        }
        //注意：这里的User是spring security的用户类，不是自定义的类
        return new User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }
}

