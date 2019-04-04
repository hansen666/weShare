package cn.compusshare.weshare.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: LZing
 * @Date: 2019/3/7
 * 外部请求工具类
 */
public class HttpUtil {

    /**
     * Get方法请求外部接口
     * @param url
     * @return
     * @throws Exception
     */
    public static String requestByGet(String url) throws Exception{
        //TODO 可加上连接超时限制
        String resultStr="";
        BufferedReader bufReader=null;
        try {
            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            //请求方式
            conn.setRequestMethod("GET");
            //设置是否从httpUrlConnection读入，默认情况下是true; httpUrlConnection.setDoInput(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
            conn.setAllowUserInteraction(false);
            //通用属性设置
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.connect();            // 获取URLConnection对象对应的输出流

//            OutputStreamWriter out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
//             out.write(param);
//             out.flush();
            //定义bufferReader读取url响应
            bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while (null != (line = bufReader.readLine())) {
                resultStr += line;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=bufReader){
                bufReader.close();
            }
        }
        return resultStr;
    }
}
