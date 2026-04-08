package com.city.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.city.constant.MessageConstant;
import com.city.constant.StatusConstant;
import com.city.constant.UserAuthStatusConstant;
import com.city.dto.UserLoginDTO;
import com.city.entity.User;
import com.city.exception.LoginFailedException;
import com.city.mapper.UserMapper;
import com.city.properties.WeChatProperties;
import com.city.service.UserService;
import com.city.utils.HttpClientUtil;

@Service
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("appid", weChatProperties.getAppid());
        reqParams.put("secret", weChatProperties.getSecret());
        reqParams.put("js_code", userLoginDTO.getCode());
        reqParams.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, reqParams);

        JSONObject parseJson = JSON.parseObject(json);
        String openid = parseJson.getString("openid");
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .authStatus(UserAuthStatusConstant.UNVERIFIED)
                    .userStatus(StatusConstant.ENABLE)
                    .balance(BigDecimal.ZERO)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}