package com.dachuang.loginserviceprovider.filter;

import com.dachuang.loginserviceprovider.exception.BaseException;
import com.dachuang.loginserviceprovider.feign.GeneratorTokenFeign;
import com.dachuang.loginserviceprovider.pojo.LoginVo;
import com.dachuang.loginserviceprovider.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/25
 */
public class LoginVerifyFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginVerifyFilter.class);

    private static final String TOKENPREFIX = "token";
    private static final String IDPREFIX = "id";

    private final ObjectMapper objectMapper;

    private final GeneratorTokenFeign generatorTokenFeign;

    private final RedisUtil redisUtil;

    public LoginVerifyFilter(AuthenticationManager authenticationManager,
                             ObjectMapper objectMapper,
                             GeneratorTokenFeign generatorTokenFeign,
                             RedisUtil redisUtil) {
        //这里我们需要更改固定的登陆拦截路径
        super(new AntPathRequestMatcher("/sharedkitchen/login",
                "POST"),authenticationManager);
        this.redisUtil=redisUtil;
        this.generatorTokenFeign=generatorTokenFeign;
        this.objectMapper=objectMapper;
    }

    /**
     private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
     new AntPathRequestMatcher("/login", "POST");
     默认拦截路径/login 和 method = POST
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws BaseException{
        String phoneNumber, password;
        LoginVo loginVo;
        try {
            loginVo = objectMapper.readValue(httpServletRequest.getInputStream(), LoginVo.class);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new BaseException(e.getMessage());
        }
        if (!Objects.isNull(loginVo)){
            password = loginVo.getPassword();
            phoneNumber = loginVo.getUsername();
        }else {
            throw new BaseException("登陆表单信息填写不完整");
        }
        phoneNumber = phoneNumber.trim(); //去除前后空格，以防万一

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                phoneNumber, password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

        //将生成的Auth实体Authentication存放进SecurityContextHolder用于校验------真实的做校验的对象是由UserDetails(这个可以自己重写)做对比
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        //一个用户一个登陆，如果当前用户已经被登陆过，那么就把另外一个登陆此用户的token令牌删除
        if (isOnline(userDetails.getUsername())){
            deleteCacheToken(userDetails.getUsername());
        }

        String token = generatorTokenFeign.generatorToken(userDetails.getUsername(),getRole(userDetails));

        log.info("[successfulAuthentication--------token----:]{}",token);
        response.setHeader(TOKENPREFIX, token);
        chain.doFilter(request,response);
    }

    private String getRole(UserDetails userDetails){
        for (GrantedAuthority authority : userDetails.getAuthorities()){
            return authority.getAuthority();
        }
        return null;
    }

    private void deleteCacheToken(String username) {
        String cacheToken = redisUtil.get(IDPREFIX+username);
        /*
               删除：
                id username -- jwt
                token jwt -- salt
         */
        redisUtil.delete(IDPREFIX+username);
        redisUtil.delete(TOKENPREFIX+cacheToken);
    }

    private Boolean isOnline(String username){
        Boolean isOnline = redisUtil.hasKey(IDPREFIX+username);
        return isOnline != null && isOnline;    //<==>isOnline == null ? false : isOnline;
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        log.info("[unsuccessfulAuthentication--------");
        SecurityContextHolder.clearContext();
        throw new BaseException("authentication failed, reason: " + failed.getMessage());
    }
}
