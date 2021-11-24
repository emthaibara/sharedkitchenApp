package com.dachuang.jwtverifyandauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Objects;

import static com.auth0.jwt.impl.PublicClaims.ISSUER;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/24
 */

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private static final String TOKENKEY = "salt";

    @Resource
    private RedisUtil redisUtil;

    //这里不考虑，用户在某个时间段升级会员后，权限变更的问题，如果变更，按照如下处理需要重写登陆，在这里我只关注role的获取
    public String getRoles(String token){
        JWTVerifier jwtVerifier = getJWTVerifier(token);
        if (jwtVerifier != null){
            return jwtVerifier.verify(token).getClaim("role").asString();
        }else {
            return null;
        }
    }

    public Boolean verifyToken(String token){
        return redisUtil.hasKey(TOKENKEY+token);
    }

    private JWTVerifier getJWTVerifier(String token){
        String salt = redisUtil.get(TOKENKEY+token);
        try {
            return JWT.require(Algorithm.HMAC256(salt))
                    .withIssuer(ISSUER)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
