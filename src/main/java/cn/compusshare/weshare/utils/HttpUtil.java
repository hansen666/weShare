package cn.compusshare.weshare.utils;

import com.alibaba.fastjson.JSONObject;

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


    /**
     * Post请求
     * @param reqUrl
     * @param jsonParam
     * @return
     */
    public static String requestByPost(String reqUrl,JSONObject jsonParam) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(reqUrl);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            // 开始连接请求
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write((jsonParam.toString()).getBytes());
            out.flush();
            out.close();
            //System.out.println(conn.getResponseCode());
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                //System.out.println("http连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("http error++");
            }

        } catch (Exception e) {

        }

        return sb.toString();

    }

}
