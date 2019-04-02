package cn.compusshare.weshare.utils;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

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
     * 空字符串判断
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s || s.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 空List判断
     *
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
     * 计算与当前的时间间隔
     *
     * @param date
     * @return
     */
    public static String timeFromNow(Date date) {
        Date now = new Date();
        long timeGap = now.getTime() - date.getTime();
        long day = timeGap / 1000 / 60 / 60 / 24;
        if (day >= 1) {
            if (day == 1) {
                return "1天前";
            }
            return getDate(date);
        }
        long hour = timeGap / 1000 / 60 / 60;
        if (hour >= 1) {
            return hour + "小时前";
        }
        long minute = timeGap / 1000 / 60;
        if (minute >= 1) {
            return minute + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 从date获取yyyy/MM/dd格式日期
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取yyyy/MM/dd HH:mm:ss格式的日期和时间
     * @param date
     * @return
     */
    public static String getFormatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 根据经纬度计算距离
     *
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

    /**
     * 文本审核
     *
     * @param text
     * @return
     */
    public static Boolean textCensor(String text) {
        AipContentCensor censor = new AipContentCensor("15804398", "cSzAUuAAbF3ZaIdMhlwDvpoM", "LyG0XwGzWaiiUcrMAoNcQlNQwincbSqg");
        JSONObject result = censor.antiSpam(text, null);
        if (((Integer) result.getJSONObject("result").get("spam")) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 图片审核
     *
     * @param fileNames
     * @param path
     * @return
     */
    public static Boolean imageCensor(String fileNames, String path) {
        String[] files = fileNames.split(",");
        AipContentCensor censor = new AipContentCensor("15804398", "cSzAUuAAbF3ZaIdMhlwDvpoM", "LyG0XwGzWaiiUcrMAoNcQlNQwincbSqg");
        for (String file : files) {
            String filePath = "D:\\WeShare\\miniprogram\\images\\" + path + "\\" + file;
            JSONObject result = censor.imageCensorUserDefined(filePath, EImgType.FILE, null);
            if (result.has("error_code")) {
                return null;
            }
            if (result.get("conclusionType").equals(2)) {
                return false;
            }
        }
        return true;
    }

}
