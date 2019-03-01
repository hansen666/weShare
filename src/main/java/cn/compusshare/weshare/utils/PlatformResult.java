package cn.compusshare.weshare.utils;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @Author: LZing
 * @Date: 2019/2/28
 * 统一格式结果返回类
 */
@JsonInclude
public class PlatformResult<T> {

    //返回码
    private int code;
    //提示信息
    private String msg;
    //返回数据
    private  T data;

    public PlatformResult(int code,T data){
        this.code=code;
        this.data=data;
    }

    public PlatformResult(int code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }


    public static <T> PlatformResult<T> success(int code,T data){
        PlatformResult<T> platformResult=new PlatformResult<T>(code,"SUCCESS",data);
        return platformResult;
    }


}
