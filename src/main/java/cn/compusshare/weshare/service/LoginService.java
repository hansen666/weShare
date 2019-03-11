package cn.compusshare.weshare.service;

import com.auth0.jwt.interfaces.Claim;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: LZing
 * @Date: 2019/3/8
 * 登录注册service
 */
@Service
public interface LoginService {

    String getToken(String code) throws Exception;

    String parseToken(String token);
}
