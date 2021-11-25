package com.dachuang.loginserviceprovider.result;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
public class Result<T> {

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
