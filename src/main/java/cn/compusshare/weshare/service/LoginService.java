package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: LZing
 * @Date: 2019/3/8
 * 登录注册service
 */
@Service
public interface LoginService {

    ResultResponse getToken(String code) throws Exception;

    String getIDFromToken(String token, String key, String IdName);

    List<String> allSchoolName();

    String adminToken(String key, String sessionId);
}
