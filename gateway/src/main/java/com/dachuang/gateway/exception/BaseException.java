package com.dachuang.gateway.exception;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/12/14
 */


public class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
