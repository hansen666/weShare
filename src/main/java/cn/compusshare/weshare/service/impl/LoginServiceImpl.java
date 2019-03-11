package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: LZing
 * @Date: 2019/3/8
 */
@Component
public class LoginServiceImpl implements LoginService {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private Environment environment;


    @Override
    public String getToken(String code) throws Exception{
        String sessionAndOpenID=getSessionKeyAndOpenID(code);
        JSONObject jsonObject=(JSONObject)JSONObject.parse(sessionAndOpenID);
        String sessionKey=jsonObject.getString("session_key");
        String openId=jsonObject.getString("openid");
        if( CommonUtil.isEmpty(sessionKey)||CommonUtil.isEmpty(openId) ){
            return Common.NULL_SESSIONKEY_OR_OPENID;
        }
        return generatorToken(sessionKey,openId);
    }

    @Override
    public String parseToken(String token) {
        JWTVerifier verifier=null;
        DecodedJWT jwt=null;
        try {
             verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("tokenKey"))).build();
             jwt=verifier.verify(token);
        }catch (Exception e){
            logger.info("token已失效");
            return null;
        }
        String openID=jwt.getClaims().get("openID").asString();
        return openID;
    }

    /**
     * 根据code获取session_key和openid
     * @param code
     * @return json字符串
     * @throws Exception
     */
    private String getSessionKeyAndOpenID(String code) throws Exception{
        String baseUrl="https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";
        String AppID="&appid="+environment.getProperty("AppID");
        String AppSecret="&secret="+environment.getProperty("AppSecret");
        String grant_type="&grant_type="+"authorization_code";
        String js_code="&js_code="+code;
        String url=baseUrl+AppID+AppSecret+grant_type+js_code;
        String result= HttpUtil.requestByGet(url);
        return result;
    }

    private String generatorToken(String sessionKey,String openID) throws Exception{
        Calendar now=Calendar.getInstance();
        //签发时间
        Date date=now.getTime();
        //过期时间
        now .add(Calendar.MINUTE,15);
        Date expireDate=now.getTime();
        //密钥
        String key=environment.getProperty("tokenKey");
        Map<String,Object> header=new HashMap<>();
        header.put("alg","HS256");
        header.put("type","JWT");

        String token= JWT.create()
                .withHeader(header)
                .withClaim("openID",openID)
                .withClaim("sessionKey",sessionKey)
                .withIssuedAt(date)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(key));
        return token;
    }
}