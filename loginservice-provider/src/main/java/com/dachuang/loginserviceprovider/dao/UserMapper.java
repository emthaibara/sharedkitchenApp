package com.dachuang.loginserviceprovider.dao;

import com.dachuang.loginserviceprovider.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
    int deleteByPrimaryKey(String phonenumber);

    int insert(User record);

    User selectByPrimaryKey(String phonenumber);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}