package com.dachuang.gateway.filter;

import com.dachuang.gateway.exception.BaseException;
import com.dachuang.gateway.util.JwtUtil;
import com.dachuang.gateway.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 *      该过滤器主要是对
 *              Jwt的有效性进行判别，如果Jwt需要刷新，那么就刷新，如果过期并且过了刷新时间，那么久返回重登的消息，直接快速失败
 *              在这里主要是做的对jwt--token的有效性（包括该请求中是否携带toke令牌，判断其合法性），并做出相应处理
 */
public class JwtFilter implements WebFilter{

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private static final List<String> IGNOREPATH = Arrays.asList("/sharedkitchen/login","/sharedkitchen/sign");

    private static final String TOKEN_PREFIX = "token";
    private static final String ID_PREFIX = "id";
    private static final Integer EXPIRED_CODE = 1;
    private static final Integer VERIFYSUCCESS_CODE = 0;


    private final RedisUtil redisUtil;

    private final JwtUtil jwtUtil;

    public JwtFilter(RedisUtil redisUtil, JwtUtil jwtUtil) {
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        ServerHttpResponse httpResponse = exchange.getResponse();
        log.info(httpRequest.getURI().getPath());
        //允许不携带token放行的url
        if (IGNOREPATH.contains(httpRequest.getURI().getPath())){
            return chain.filter(exchange);
        }

        //获取request后从请求中获取token
        final String token = getToken(httpRequest);

        //无携带token直接快速失败
        if (Objects.isNull(token)){
            throw new BaseException("不合法的请求，请携带令牌访问");
        }

        log.info(token);
        checkToken(httpResponse,token);
        return chain.filter(exchange);
    }

    /*
            token刷新策略：
                jwt是否过期,以及是否符合jwt规范？
                    过期---->检查redis缓存中是否存在该token--->存在刷新，不存在直接快速失败返回提示信息
                    不符合jwt规范---->返回提示信息
                    没果期---->放行
    */
    private void checkToken(ServerHttpResponse response,String token){
        //decoder jwt的时候，非法的token被异常捕捉
        String username = jwtUtil.getSubject(token);
        String salt;
        //检查缓存中是否存在key
        if (Objects.isNull(salt = redisUtil.get(TOKEN_PREFIX+token))){
            throw new BaseException("登陆超时，请重新登陆");}
        //检查jwt是否合法
        Integer verifyResult = jwtUtil.verifier(token,salt);
        if (verifyResult.equals(VERIFYSUCCESS_CODE))
            return;
        if (verifyResult.equals(EXPIRED_CODE)) {
            //刷新
            deleteCache(username, token);
            response.getHeaders().set(TOKEN_PREFIX, jwtUtil.generateToken(username, jwtUtil.getDecodedJWT(token).getClaim("role").asString()));
            return;
        }
        throw new BaseException(jwtUtil.getVerifyMessage(token,salt));
    }

    private void deleteCache(String username,String token) {
        redisUtil.delete(ID_PREFIX+username);
        redisUtil.delete(TOKEN_PREFIX+token);
    }

    private String getToken(ServerHttpRequest httpRequest){
        return httpRequest.getHeaders().getFirst(TOKEN_PREFIX);
    }

}
