package cn.compusshare.weshare.utils;


/**
 * @Author: LZing
 * @Date: 2019/3/4
 * 统一结果返回类
 */

public class ResultResponse<T> {
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体的内容. */
    private T data;

    public ResultResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }




}
