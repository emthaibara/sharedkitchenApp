package com.dachuang.generatetokenserviceprovider.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static com.auth0.jwt.impl.PublicClaims.ISSUER;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/3
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    //设置过期失效
    private static final long EXPIREDTIME = 15L;

    public static final String TOKENKEY = "token";
    private static final String IDPREFIX = "id";
    @Resource
    private RedisUtil redisUtil;

    public JwtUtil() {
    }

    //生成token---这里后期会拓展，暂时测试的
    public String generateToken(String username,String role) {
        String salt = BCrypt.gensalt();
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)    //设置主题
                .withClaim("role",role)
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIREDTIME)) //过期失效
                .withIssuedAt(new Date())       //发行时间
                .sign(Algorithm.HMAC256(salt));
        log.info("username:"+username+" token:"+token+" salt:"+salt);
        //redis缓存token-----salt并设置过期时间
        redisUtil.set(TOKENKEY+token,salt,RedisUtil.TOKEN_EXPIREDTIME);
        redisUtil.set(IDPREFIX+username,token,RedisUtil.TOKEN_EXPIREDTIME);
        return token;
    }


}
