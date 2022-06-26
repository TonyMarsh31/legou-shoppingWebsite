package com.lxs.legou.security.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.security.config.TokenDecode;
import com.lxs.legou.security.po.Address;
import com.lxs.legou.security.service.IAddressService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/address")
public class AddressController extends BaseController<IAddressService, Address> {

    final private TokenDecode tokenDecode;

    public AddressController(TokenDecode tokenDecode) {
        this.tokenDecode = tokenDecode;
    }

    @Override
    @ApiOperation(value = "查询用户收货地址")
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Address> list(Address entity) {
        Map<String, String> user;
        try {
            //通过传递的JWT来获取用户名称
            user = tokenDecode.getUserInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = user.get("user_name");
        entity.setUsername(username);
        return service.list(entity);
    }
}
