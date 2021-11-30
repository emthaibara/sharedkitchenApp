package com.dachuang.loginserviceprovider.pojo;

import com.dachuang.loginserviceprovider.dao.UserMapper;
import com.dachuang.loginserviceprovider.exception.BaseException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/25
 */
@Component
public class LoginVo implements UserDetailsService {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    //username = phoneNumber/email/uuid(username不是一个单一属性)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = userMapper.selectByPrimaryKey(username).getPassword();
        if (Objects.isNull(password)){
            //快速失败
            throw new BaseException("该用户不存在");
        }
        return User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(userMapper.selectByPrimaryKey(username).getRole())
                .build();
    }

}
