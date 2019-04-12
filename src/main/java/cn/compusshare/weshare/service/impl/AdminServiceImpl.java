package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.entity.Admin;
import cn.compusshare.weshare.repository.mapper.AdminMapper;
import cn.compusshare.weshare.service.AdminService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.service.common.CacheService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminServiceImpl implements AdminService {

    private final static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private Environment environment;


    /**
     * 管理员登录
     * @param account
     * @param password
     * @param session
     * @return
     */
    @Override
    public ResultResponse login(String account, String password, HttpSession session) {
        String token = cacheService.getString(account);
        //如果缓存中有token，说明之前已有人登录
        if (! CommonUtil.isEmpty(token)) {
            return ResultUtil.fail(Common.LOGIN_ALREADY, Common.LOGIN_ALREADY_MSG);
        }
        Admin admin = adminMapper.selectByPrimaryKey(account);
        //md5加密
        password = DigestUtils.md5Hex(password);
        //密码不相等
        if (! password.equals(admin.getPassword())) {
            return ResultUtil.fail(Common.WRONG_PASSWORD, Common.WRONG_PASSWORD_MSG);
        }
        token = loginService.adminToken(environment.getProperty("weShareAdmin@&!!!8900"),session.getId());
        Map<String,String> result = new HashMap<>(1);
        result.put("token",token);
        return ResultUtil.success(result);
    }

}
