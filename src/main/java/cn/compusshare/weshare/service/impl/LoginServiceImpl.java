package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.service.UserService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.HttpUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Author: LZing
 * @Date: 2019/3/8
 */
@Component
public class LoginServiceImpl implements LoginService {

    private final static Logger logger= LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolMapper schoolMapper;


    /**
     * 根据code获取sessionKey和openID，并生成token
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public ResultResponse getToken(String code) throws Exception{
        String sessionAndOpenID=getSessionKeyAndOpenID(code);
        JSONObject jsonObject=(JSONObject)JSONObject.parse(sessionAndOpenID);
        String sessionKey=jsonObject.getString("session_key");
        String openId=jsonObject.getString("openid");
        if( CommonUtil.isEmpty(sessionKey)||CommonUtil.isEmpty(openId) ){
            return ResultUtil.fail(Common.CODE_INVALID,Common.CODE_INVALID_MSG);
        }
        Map<String,Object> result=new HashMap<>();
        String token=generatorToken(sessionKey,openId);
        boolean isNewUser=!userService.isUserExist(openId);
        result.put("token",token);
        result.put("isNewUser",isNewUser);
        return ResultUtil.success(result);
    }

    /**
     * 从token中解析出ID
     * @param token
     * @param key
     * @param IdName
     * @return
     */
    @Override
    public String getIDFromToken(String token, String key, String IdName) {
        JWTVerifier verifier=null;
        DecodedJWT jwt=null;
        try {
             verifier = JWT.require(Algorithm.HMAC256(key)).build();
             jwt=verifier.verify(token);
        }catch (Exception e){
            logger.info("token解析错误"+e.getMessage());
            return null;
        }
        String ID=jwt.getClaims().get(IdName).asString();
        return ID;
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

    /**
     * 生成token
     * @param sessionKey
     * @param openID
     * @return
     * @throws Exception
     */
    private String generatorToken(String sessionKey,String openID) throws Exception{
        Calendar now=Calendar.getInstance();
        //签发时间
        Date date=now.getTime();
        //过期时间
        now .add(Calendar.MINUTE,60);
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

    /**
     * 选择所有学校名
     *
     * @return
     */
    @Override
    public List<String> allSchoolName() {

        return schoolMapper.selectAllName();
    }


    /**
     * 生成管理员token
     * @param sessionId
     * @Param key
     * @return
     */
    @Override
    public String adminToken(String key, String sessionId) {
        Calendar now=Calendar.getInstance();
        //签发时间
        Date date=now.getTime();
        //过期时间
        //now .add(Calendar.MINUTE,60);
        // Date expireDate=now.getTime();
        //密钥

        Map<String,Object> header=new HashMap<>();
        header.put("alg","HS256");
        header.put("type","JWT");

        String token = null;
        try {
            token= JWT.create()
                    .withHeader(header)
                    .withClaim("sessionId",sessionId)
                    .withIssuedAt(date)
                    .sign(Algorithm.HMAC256(key));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return token;
    }
}
