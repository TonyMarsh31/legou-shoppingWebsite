package com.lxs.legou.security.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.core.po.ResponseBean;
import com.lxs.legou.security.dto.UserLoginParamDto;
import com.lxs.legou.security.po.Role;
import com.lxs.legou.security.po.User;
import com.lxs.legou.security.service.IUserService;
import com.lxs.legou.security.utils.BPwdEncoderUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController<IUserService, User> {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final OAuth2ClientProperties oAuth2ClientProperties; //包含了client_id, client_secret等信息
    private final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails; //包含了access-token-uri, grant_type等信息
    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate, OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, OAuth2ClientProperties oAuth2ClientProperties) {
        this.restTemplate = restTemplate;
        this.oAuth2ProtectedResourceDetails = oAuth2ProtectedResourceDetails;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @RequestMapping("/login")
    public ResponseEntity<OAuth2AccessToken> login(@Valid UserLoginParamDto loginDto, BindingResult bindingResult) throws Exception {
        //1:验证用户
        if (bindingResult.hasErrors()) {
            throw new Exception("登陆信息错误，请确认后再试");
        }
        User user = service.getUserByUserName(loginDto.getUsername());
        if (null == user) {
            throw new Exception("用户不存在");
        }
        //传输的是明文密码，需要加密后再比较
        if (!BPwdEncoderUtil.matches(loginDto.getPassword(), user.getPassword())) {
            throw new Exception("密码错误，请确认后再试");
        }
        //2:用户验证通过,使用restTemplate发送请求到授权服务器，申请令牌
        //请求头 "basic auth"
        String client_secret = oAuth2ClientProperties.getClientId() + ":" + oAuth2ClientProperties.getClientSecret();
        client_secret = "Basic " + Base64.getEncoder().encodeToString(client_secret.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", client_secret);
        //上述过程构建发送http请求的授权信息,即使用Postman测试时位于请求头中的Authorization字段

        //请求参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", Collections.singletonList(loginDto.getUsername()));
        map.put("password", Collections.singletonList(loginDto.getPassword()));
        map.put("grant_type", Collections.singletonList(oAuth2ProtectedResourceDetails.getGrantType()));
        map.put("scope", oAuth2ProtectedResourceDetails.getScope());
        //上述过程构建发送http请求的参数信息,即请求体中的username,password,grant_type,scope字段

        //HttpEntity(请求参数 + 头 ...)
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        //获取Token 注意先配置好oauth2.client.access-token-uri
        return restTemplate.exchange(oAuth2ProtectedResourceDetails.getAccessTokenUri(), HttpMethod.POST, httpEntity, OAuth2AccessToken.class);
        //上述过程即使用密码模式登陆，获取access_token

    }

    @ApiOperation("通过登录获得用户")
    @GetMapping("/get/{userName}")
    public User getByUserName(@PathVariable("userName") String userName) {
        return service.getUserByUserName(userName);
    }

    @ApiOperation("通过用户ID获得角色")
    @GetMapping("/select-roles/{id}")
    public List<Role> selectRolesByUserId(@PathVariable("id") Long id) {
        return service.selectRoleByUser(id);
    }

    /**
     * 验证用户名是否存在
     */
    @ApiOperation("验证用户名是否存在")
    @PostMapping("/validate-name/{userName}")
    public String validUserName(@PathVariable String userName, Long id) {
        long rowCount = service.findCountByUserName(userName);

        //修改时=原来的用户名
        if (id != null) {
            User user = service.getById(id);
            if (null != userName && userName.equals(user.getUserName())) {
                return "{\"success\": true}";
            }
        }

        if (rowCount > 0) {
            return "{\"success\": false}";
        } else {
            return "{\"success\": true}";
        }
    }

    /**
     * 锁定用户
     */
    @GetMapping("/lock/{id}")
    @ApiOperation("锁定账户")
    public ResponseBean lock(@PathVariable Long id) {
        ResponseBean rm = new ResponseBean();
        try {
            User u = service.getById(id);

            User user = new User();
            user.setId(id);
            if (null != u.getLock() && u.getLock()) {
                rm.setMsg("用户已解锁");
                user.setLock(false);
            } else {
                rm.setMsg("用户已锁定");
                user.setLock(true);
            }
            service.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMsg("保存失败");
        }

        return rm;
    }

    @Override
    public void afterEdit(User domain) {
        //生成角色列表, 如：1,3,4
        List<Role> roles = service.selectRoleByUser(domain.getId());
        Long[] ids = new Long[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            ids[i] = roles.get(i).getId();
        }
        domain.setRoleIds(ids);
    }

    /**
     * 增加积分
     *
     * @param point    积分
     * @param username 用户名
     */
    @GetMapping(value = "/add-point")
    public void addPoint(@RequestParam("point") Long point, @RequestParam("username") String username) {
        service.addPoint(point, username);
    }


}
