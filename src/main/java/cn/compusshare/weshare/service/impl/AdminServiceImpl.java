package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.entity.Admin;
import cn.compusshare.weshare.repository.mapper.AdminMapper;
import cn.compusshare.weshare.repository.mapper.UserMapper;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AdminServiceImpl implements AdminService {

    private final static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private Environment environment;

    @Override
    public ResultResponse userQuery(int type){
        try {
            List<Map<String, Object>> userList = userMapper.selectUserByType(type);
            return ResultUtil.success(userList);
        }catch (Exception e){
            logger.info("用户数据库查询错误");
            return  ResultUtil.fail(-1, e.getMessage());
        }
    }




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
        token = loginService.adminToken(environment.getProperty("adminTokenKey"),session.getId());
        cacheService.set(account,token, Long.valueOf(environment.getProperty("overdueTime")), TimeUnit.SECONDS);
        Map<String,String> result = new HashMap<>(1);
        result.put("token",token);
        logger.info(account+"成功登录");
        return ResultUtil.success(result);
    }

    /**
     * 退出登录
     * @param account
     * @param currentToken
     * @return
     */
    @Override
    public ResultResponse logout(String account, String currentToken) {
        String token = cacheService.getString(account);
        //token为空，直接退出
        if (CommonUtil.isEmpty(token)) {
            logger.info(account+"退出登录");
            return ResultUtil.success();
        }

        if (! currentToken.equals(token)) {
            return ResultUtil.fail(Common.TOKEN_INVALID_OR_ACCOUNT_ERROR,Common.TOKEN_INVALID_OR_ACCOUNT_ERROR_MSG);
        }
        //删除token缓存
        cacheService.del(account);
        logger.info(account+"退出登录");
        return ResultUtil.success();
    }


}
