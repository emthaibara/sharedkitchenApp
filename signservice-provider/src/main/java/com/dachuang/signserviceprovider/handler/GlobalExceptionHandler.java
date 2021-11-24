package com.dachuang.signserviceprovider.handler;

import com.dachuang.signserviceprovider.exception.BaseException;
import com.dachuang.signserviceprovider.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/17
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

    @ResponseBody
    @ExceptionHandler(value = {BindException.class})
    public Result<?> validatedExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).
                collect(Collectors.joining());
        log.error(message);
        return new Result<>().error(message,null);
    }

}
