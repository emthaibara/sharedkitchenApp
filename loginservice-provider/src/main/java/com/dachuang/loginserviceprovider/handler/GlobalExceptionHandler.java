package com.dachuang.loginserviceprovider.handler;

import com.dachuang.loginserviceprovider.exception.BaseException;
import com.dachuang.loginserviceprovider.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/25
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public Result<?> remoteExceptionHandler(BaseException e) {
        log.error(e.getMessage());
        return new Result<>().error(e.getMessage(),null);
    }
}
