package cn.compusshare.weshare.utils;


import cn.compusshare.weshare.constant.ResultEnum;

/**
 * @Author: LZing
 * @Date: 2019/3/4
 * 结果返回包装工具类
 */
public class ResultUtil {
    public static ResultResponse success(Object object) {
        ResultResponse resultResponse = new ResultResponse(ResultEnum.success.getCode(),ResultEnum.success.getMsg(),object);
        return resultResponse;
    }

    public static ResultResponse success() {
        return success(null);
    }

    public static ResultResponse success(Integer code,String msg,Object object){
        ResultResponse resultResponse=new ResultResponse(code,msg,object);
        return resultResponse;
    }

    public static ResultResponse fail(Integer code, String msg) {
        ResultResponse resultResponse = new ResultResponse(code,msg);
        return resultResponse;
    }

    public static ResultResponse fail(){
        ResultResponse resultResponse=new ResultResponse(ResultEnum.fail.getCode(),ResultEnum.fail.getMsg());
        return resultResponse;
    }
}
