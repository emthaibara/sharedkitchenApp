package com.dachuang.signserviceprovider.pojo;


import com.dachuang.signserviceprovider.validator.annotation.IsIdCard;
import com.dachuang.signserviceprovider.validator.annotation.IsLegalRole;
import com.dachuang.signserviceprovider.validator.annotation.IsMobile;
import com.dachuang.signserviceprovider.validator.annotation.IsRegisteredSchool;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
public class SignVo implements Serializable {

    private static final long serialVersionUID = -1242493306307174690L;

    @IsMobile
    private String phoneNumber;

    @IsIdCard
    private String idCard;

    @NotNull(message = "password must not null")//后续密码的要求可能有变动，目前不能为null，只有前台校验
    private String password;

    @IsRegisteredSchool
    private String school;

    @IsLegalRole
    private String role;

    @NotNull(message = "code must not null")
    private String code; //验证码 ----> 首先设置两个Filter首先拦截code如果auth success ---->放行，否则返回Result

    @Override
    public String toString() {
        return "SignVo{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", idCard='" + idCard + '\'' +
                ", password='" + password + '\'' +
                ", school='" + school + '\'' +
                ", role='" + role + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
