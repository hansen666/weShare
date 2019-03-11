package cn.compusshare.weshare.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @Author: LZing
 * @Date: 2019/3/7
 * 通用工具类
 */
public class CommonUtil {


    public static boolean isNullObject(Object o){
        if(null != o){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String s){
        if( null==s || s.length()==0 ){
            return true;
        }
        return false;
    }



    public static Properties getProperties() throws Exception{
        Properties properties=new Properties();
        properties.load(Object. class .getResourceAsStream( "/src/main/resource/application.properties" ));
        return properties;
    }
}
