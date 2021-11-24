package com.dachuang.jwtverifyandauth.service;

import com.dachuang.jwtverifyandauth.util.JwtUtils;
import com.dachuang.jwtverifyandauth.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */

@Service
public class VerifyAuthService {

    @Resource
    private JwtUtils jwtUtils;

    public Boolean Verify(String token){
        return jwtUtils.verifyToken(token);
    }

    public String auth(String token){
        return jwtUtils.getRoles(token);
    }
}
