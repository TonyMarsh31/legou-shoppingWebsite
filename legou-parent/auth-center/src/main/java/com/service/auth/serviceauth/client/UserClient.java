package com.service.auth.serviceauth.client;

import com.lxs.legou.security.api.UserApi;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "security-service", fallback = UserClient.UserClientFallback.class)
public interface UserClient extends UserApi {

    @Component
    @RequestMapping("/fallback") //这个可以避免容器中requestMapping重复
    class UserClientFallback implements UserClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(UserClientFallback.class);

        @Override
        public User getByUserName(String userName) {
            LOGGER.info("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public List<Role> selectRolesByUserId(Long id) {
            LOGGER.info("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public void addPoint(Long point, String username) {
            LOGGER.info("异常发生，进入fallback方法");
        }
    }

}
