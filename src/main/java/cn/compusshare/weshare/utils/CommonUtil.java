package cn.compusshare.weshare.utils;


import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/3/7
 * 通用工具类
 */
public class CommonUtil {


    /**
     * 空Object判断
     * @param o
     * @return
     */
    public static boolean isNullObject(Object o){
        if(null != o){
            return true;
        }
        return false;
    }

    /**
     *空字符串判断
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        if( null==s || s.length()==0 ){
            return true;
        }
        return false;
    }

    /**
     * 空List判断
     * @param list
     * @return
     */
    public static boolean isNullList(List list){
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }




}
