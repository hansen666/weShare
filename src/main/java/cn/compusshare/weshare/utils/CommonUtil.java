package cn.compusshare.weshare.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/3/7
 * 通用工具类
 */
public class CommonUtil {


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
    public static boolean isNullList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 从date获取日期
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 根据经纬度计算距离
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {

        double EARTH_RADIUS = 6371000;//赤道半径(单位m)
        double INTEGR_NUM = 10000;

        double x1 = Math.cos(latitude1) * Math.cos(longitude1);
        double y1 = Math.cos(latitude1) * Math.sin(longitude1);
        double z1 = Math.sin(latitude1);
        double x2 = Math.cos(latitude2) * Math.cos(longitude2);
        double y2 = Math.cos(latitude2) * Math.sin(longitude2);
        double z2 = Math.sin(latitude2);
        double lineDistance =
                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        double s = EARTH_RADIUS * Math.PI * 2 * Math.asin(0.5 * lineDistance) / 180;
        return Math.round(s * INTEGR_NUM) / INTEGR_NUM;
    }


}
