package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;


/**
 * @Author: LZing
 * @Date: 2019/3/8
 * 登录注册service
 */
@Service
public interface LoginService {

    ResultResponse getToken(String code) throws Exception;

    String getOpenIDFromToken(String token);
}
