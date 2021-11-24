package com.dachuang.signserviceprovider.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
@Component
public class SignFilter implements Filter {

    private final static Logger log = LoggerFactory.getLogger(SignFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
