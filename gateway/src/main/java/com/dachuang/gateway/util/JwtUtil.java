package com.dachuang.gateway.util;

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
 * @time:2021/11/29
 */

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static final String TOKENPREFIX = "token";
    private static final String IDPREFIX = "id";
    private static final long EXPIREDTIME = 15L;
    @Resource
    private RedisUtil redisUtil;

    //判断Jwt是否过期
    public Boolean isExpired(String token){
        Date expired = JWT.decode(token).getExpiresAt();
        return expired.before(getJwtIssuedAt(token));
    }

    //获取Jwt签发时间
    private Date getJwtIssuedAt(String token){
        return JWT.decode(token)
                .getIssuedAt();
    }


    //生成token
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
        redisUtil.set(TOKENPREFIX+token,salt,RedisUtil.TOKEN_EXPIREDTIME);
        return token;
    }

}
