package cn.compusshare.weshare.constant;

/**
 * @Author: LZing
 * @Date: 2019/2/28
 * 公共常量
 */
public class Common {

    //接口返回码
    public final static int SUCCESS=0;//成功
    public final static int FAIL=-1;//失败

    public final static int TOKEN_INVALID=1;  //token失效
    public final static int TOKEN_NULL=2;  //token为空
    public final static int CODE_INVALID=1;  //code无效


    public final static String CODE_INVALID_MSG="code无效";
    public final static String TOKEN_INVALID_MSG="token已失效";
    public final static String TOKEN_NULL_MSG="token为空";
    public final static String DATABASE_OPERATION_FAIL="数据库操作失败或用户已存在";



}
