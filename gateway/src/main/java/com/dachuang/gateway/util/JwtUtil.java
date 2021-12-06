package com.dachuang.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
    private static final long EXPIREDTIME = 1000*15L;

    @Resource
    private RedisUtil redisUtil;

    public DecodedJWT getDecodedJWT(String token) {
        return JWT.decode(token);
    }

    public String getSubject(String token){
        return JWT.decode(token).getSubject();
    }

    private Integer JwtVerify(String token,String salt){
        try {
            JWT.require(Algorithm.HMAC256(salt))
                    .withIssuer(ISSUER)
                    .build().verify(token);
        }catch (AlgorithmMismatchException e){
            System.out.println(e.getMessage());
            return 1;
        }catch (TokenExpiredException e){
            System.out.println(e.getMessage());
            return 2;
        }catch (JWTVerificationException e){
            System.out.println(e.getMessage());
            return 3;
        }
        return 0;
    }
    //生成token
    public String generateToken(String username,String role) {
        String salt = BCrypt.gensalt();
        System.out.println(salt);
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

    public Integer verifier(String token,String salt) {
        //验证结果有三种，salt 错误 ， token过期 ， token 格式错误,0 1 2
        return JwtVerify(token,salt);
    }

}
