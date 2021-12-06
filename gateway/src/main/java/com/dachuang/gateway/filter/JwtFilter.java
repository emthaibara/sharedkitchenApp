package com.dachuang.gateway.filter;

import com.dachuang.gateway.result.Result;
import com.dachuang.gateway.util.JwtUtil;
import com.dachuang.gateway.util.RedisUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/29
 *      该过滤器主要是对
 *              Jwt的有效性进行判别，如果Jwt需要刷新，那么就刷新，如果过期并且过了刷新时间，那么久返回重登的消息，直接快速失败
 *              在这里主要是做的对jwt--token的有效性（包括该请求中是否携带toke令牌，判断其合法性），并做出相应处理
 */
@Component
public class JwtFilter implements WebFilter {

    private static final String TOKENPREFIX = "token";

    private static final long REFRESHTIME = 15L;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        ServerHttpResponse httpResponse = exchange.getResponse();
        //获取request后从请求中获取token
        String token = getToken(httpRequest);
        //无token直接快速失败
        if (Objects.isNull(token)){
            return Mono.error(new AccessDeniedException(Result.resultToJsonString(new Result<>().error("不合法的请求，请携带令牌访问",null))));
        }
        //截取到了jwt-----检查是否过期以及是否需要刷新令牌，获得权限+username+以及其他附带的数据/这里暂时无
        if (!isExit(token)){
            //快速失败，通知用户登陆过期，重新登陆
            return Mono.error(new AccessDeniedException(Result.resultToJsonString(new Result<>().error("登陆过期，请重新登陆",null))));
        }
        String salt = redisUtil.get(TOKENPREFIX+token);
        //判断token是否过期，如果过期则刷新token，修改请求头的token
        if (jwtUtil.verifier(token,salt) == 2){

        }
        return chain.filter(exchange);
    }

    //判断是否需要刷新token
    private Boolean isExit(String token){
        return redisUtil.hasKey(TOKENPREFIX+token);
    }

    private String getToken(ServerHttpRequest httpRequest){
        return httpRequest.getHeaders().getFirst(TOKENPREFIX);
    }

}
