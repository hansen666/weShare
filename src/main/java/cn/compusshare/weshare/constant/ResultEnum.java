package cn.compusshare.weshare.constant;

/**
 * @Author: LZing
 * @Date: 2019/3/4
 * 结果枚举
 */
public enum ResultEnum {
    success(0,"success"),
    fail(-1,"fail"),
    ;

    private Integer code;
    private String msg;



    ResultEnum(int code, String msg) {
        this.code=code;
        this.msg=msg;
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



}
