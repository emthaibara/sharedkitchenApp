package com.dachuang.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
            log.error(e.getMessage());
            return 1;
        }catch (TokenExpiredException e){
            log.error(e.getMessage());
            return 2;
        }catch (JWTVerificationException e){
            log.error(e.getMessage());
            return 3;
        }
        return 0;
    }

    public String getVerifyMessage(String token,String salt){
        try {
            JWT.require(Algorithm.HMAC256(salt))
                    .withIssuer(ISSUER)
                    .build().verify(token);
        }catch (AlgorithmMismatchException | TokenExpiredException e){
            log.error(e.getMessage());
            return e.getMessage();
        }
        return "ok";
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
        redisUtil.set(IDPREFIX+username,token);

        return token;
    }

    public Integer verifier(String token,String salt) {
        return JwtVerify(token,salt);
    }

}
