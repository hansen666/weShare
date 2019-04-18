package cn.compusshare.weshare.constant;

/**
 * @Author: LZing
 * @Date: 2019/2/28
 * 公共常量
 */
public class Common {

    //接口返回码
    public final static int SUCCESS = 0;//成功
    public final static int FAIL = -1;//失败

    public final static int TOKEN_INVALID = -4;  //token失效
    public final static int TOKEN_NULL = -3;  //token为空
    public final static int CODE_INVALID = -2;  //code无效

    //图片审核
    public final static int CENSOR_FAIL = 1; //审核未通过
    public final static int CENSOR_TIMES_LIMIT = 2; //审核次数受限


    //接口返回提示信息
    public final static String SUCCESS_MSG = "success";
    public final static String FAIL_MSG = "fail";
    public final static String CODE_INVALID_MSG = "code无效";
    public final static String TOKEN_INVALID_MSG = "token已失效";
    public final static String TOKEN_NULL_MSG = "token为空";
    public final static String DATABASE_OPERATION_FAIL = "数据库操作失败";
    public final static String CENSOR_TIMES_LIMIT_MSG = "审核次数受限";
    public final static String CENSOR_FAIL_MSG = "审核未通过";



}
