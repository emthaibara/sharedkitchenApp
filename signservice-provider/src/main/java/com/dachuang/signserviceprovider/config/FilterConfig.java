package com.dachuang.signserviceprovider.config;

import com.dachuang.signserviceprovider.filter.SignFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */

@Configuration
public class FilterConfig {

    @Resource
    private SignFilter signFilter;

    @Bean
    public FilterRegistrationBean<SignFilter> registerSignFilter(){
        FilterRegistrationBean<SignFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(signFilter);
        filterRegistrationBean.addUrlPatterns("/sharedkitchen/sign");
        return filterRegistrationBean;
    }

}

