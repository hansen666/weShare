package cn.compusshare.weshare.utils;


import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author: LZing
 * @Date: 2019/3/13
 */
public class CommonUtilTest {

    @Test
    public void get() throws Exception{

        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
        url = url + "20_ynt44K23tnS8HNZoia_sNQyAO6bPeqi-1SZBnIwebggixWMP5bkYjfSyIP2zt2YGJVstuzqFi0JyA6Fj6re-I8TqmiJZFbMyk8BiUCUDVybyrGtFbDX9yDaf4KfFkrv4K6kft7ZNTQ8BsSYOOYTeAFAAKF";
//        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx8c47e26826722ada&secret=f0b1893ae1302ffdda00312547453906";
//        String tokenStr = HttpUtil.requestByGet(getTokenUrl);
       // String tokenStr = "{\"access_token\":\"20_U13t6ol6qeG8rkG16iHbDTdYx17A64Yq09v6TAnc2sfFbd0xMlrJ6Lt-qH9MIK9HtHV88r1coKDzj_F-tuAlrUYJ9OF1lWMZNPEKUHVJR_rJvww1WkQ1hSJ0sk14a8FASgl7Gp4M4zC3Yi5tKXXeAFAWJJ\",\"expires_in\":7200}";
//        JSONObject tokenObj = (JSONObject) JSONObject.parse(tokenStr);
//        if (tokenObj.containsKey("access_token")) {
//            String token = tokenObj.getString("access_token");
//            System.out.println(token);
//        }


        Map<String,Object> map = new HashMap<>();
        map.put("touser","oRpmu4svAH_Voq3peuz8rUGzYA1s");
        map.put("msgtype","text");
        Map<String,Object> tempMap = new HashMap<>(1);
        tempMap.put("content","欢迎来撩客服小姐姐!\n1、第一个问题\n2、第二个问题");
        map.put("text",tempMap);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(map);
        System.out.println(jsonObject.toJSONString());
        String result = HttpUtil.requestByPost(url,jsonObject);
        System.out.println(result);
    }

    @Test
    public void fun() throws Exception {
        String is="a456";
        System.out.println(is.matches("^\\d+$"));
    }
}