package com.dachuang.gateway.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
public class Result<T> {

    public static final Logger log = LoggerFactory.getLogger(Result.class);

    private Integer code ;
    private String msg;
    private T data;

    public Result() {}

    public Result(CodeMsg codeMsg){
        this.code = codeMsg.getCode();
        this.data = null;
        this.msg = codeMsg.getCodeMsg();
    }
    public Result<T> error(String msg, T data){
        this.code = -1;
        this.msg = msg;
        this.data = data;
        return this;
    }
    public Result(T data) {
        this.code = 200 ;
        this.data = data;
        this.msg= null;
    }

    public static Result<String> signSuccess() {
        return new Result<>("Sign success");
    }

    public static Result<?> tokenExpired(){
        return new Result<>().error("登陆过期请重新登陆",null);
    }

    public static String resultAsJsonString(Result<?> result){
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(result);
            return json;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }finally {
            objectMapper.clearProblemHandlers();
        }
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String codeMsg) {
        this.msg = codeMsg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
