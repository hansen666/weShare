package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.entity.Admin;
import cn.compusshare.weshare.repository.mapper.AdminMapper;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.repository.mapper.WantGoodsMapper;
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
import java.util.Date;
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
    private PublishGoodsMapper publishGoodsMapper;

    @Autowired
    private WantGoodsMapper wantGoodsMapper;

    @Autowired
    private Environment environment;

    /**
     * 用户查询
     *
     * @param  type
     * @param  currentPage
     * @return
     */
    @Override
    public ResultResponse userQuery(int type, int currentPage) {
        try {
            List<Map<String, Object>> userList = userMapper.selectUserByType(type, currentPage * 7);
            return ResultUtil.success(userList);
        } catch (Exception e) {
            logger.info("userQuery用户数据库查询错误"+e.getMessage());
            return ResultUtil.fail(Common.FAIL, e.getMessage());
        }
    }

    /**
     * 管理员登录
     *
     * @param account
     * @param password
     * @param session
     * @return
     */
    @Override
    public ResultResponse login(String account, String password, HttpSession session) {
        Admin admin = adminMapper.selectByPrimaryKey(account);
        //md5加密
        password = DigestUtils.md5Hex(password);
        //密码不相等
        if (!password.equals(admin.getPassword())) {
            return ResultUtil.fail(Common.WRONG_PASSWORD, Common.WRONG_PASSWORD_MSG);
        }

        String token = cacheService.getString(account);

        if (!CommonUtil.isEmpty(token)) {
            token = refreshToken(account, environment.getProperty("adminTokenKey"), session.getId());
            logger.info(account + "登录成功");
        }else {  //如果缓存中有token，说明之前已有人登录,把另一账号挤下线
            token = refreshToken(account, environment.getProperty("adminTokenKey"), session.getId());
            logger.info(account + "登录成功，另一地点登录的账号被迫下线");
        }

        Map<String, String> result = new HashMap<>(1);
        result.put("token", token);
        return ResultUtil.success(result);
    }

    public String refreshToken(String account, String key, String claim) {
        String token = loginService.adminToken(key, claim);
        cacheService.set(account, token, Long.valueOf(environment.getProperty("overdueTime")), TimeUnit.MINUTES);
        return token;
    }

    /**
     * 退出登录
     *
     * @param account
     * @param currentToken
     * @return
     */
    @Override
    public ResultResponse logout(String account, String currentToken) {
        String token = cacheService.getString(account);
        //token为空，直接退出
        if (CommonUtil.isEmpty(token)) {
            logger.info(account + "退出登录");
            return ResultUtil.success();
        }

        if (!currentToken.equals(token)) {
            return ResultUtil.fail(Common.TOKEN_INVALID_OR_ACCOUNT_ERROR, Common.TOKEN_INVALID_OR_ACCOUNT_ERROR_MSG);
        }
        //删除token缓存
        cacheService.del(account);
        logger.info(account + "退出登录");
        return ResultUtil.success();
    }

    /**
     * 统计某一年中每月发布的物品数量
     *
     * @param year
     * @return
     */
    @Override
    public ResultResponse monthlyPublishGoodsQuantity(int year) {
        if (year < 0) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = publishGoodsMapper.monthlyQuantity(year);
        return ResultUtil.success(resultMap);
    }

    /**
     * 某月中每日的物品发布量
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public ResultResponse dailyPublishGoodsQuantity(int year, int month) {
        if (year < 0 || (month < 1 || month > 12)) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = publishGoodsMapper.dailyQuantity(year, month);
        return ResultUtil.success(resultMap);
    }

    /**
     * 某年的用户注册数量
     *
     * @param year
     * @return
     */
    @Override
    public ResultResponse monthlyUserQuantity(int year){
        if (year < 0) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = userMapper.monthlyQuantity(year);
        return ResultUtil.success(resultMap);
    }

    /**
     * 某年某月的用户注册数量
     *
     * @param year
     * @param month
     * @return
     */
    public ResultResponse dailyUserQuantity(int year, int month){
        if (year < 0 || (month < 1 || month > 12)) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = userMapper.dailyQuantity(year, month);
        return ResultUtil.success(resultMap);
    }
    /**
     * 查询审核未通过的物品
     * @param currentPage 分页
     * @param flag 标识，发布的或求购的
     * @return
     */
    @Override
    public ResultResponse auditFailGoods(int currentPage, int flag) {
        List<Map<String, Object>> result;
        if (flag == 0) {
            result = publishGoodsMapper.auditFailGoods(currentPage * 0);
        }else {
            result = wantGoodsMapper.auditFailGoods(currentPage * 10);
        }
        result.forEach(map->map.put("pubTime",CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return ResultUtil.success(result);
    }

    /**
     * 更新物品状态
     * @param id
     * @param status
     * @param flag
     * @return
     */
    @Override
    public ResultResponse changeGoodsStatus(int id, byte status, int flag) {
        int result;
        if (flag == 0) {
            result = publishGoodsMapper.updateStatus(id, status);
        }else {
            result = wantGoodsMapper.updateStatus(id, status);
        }
        logger.info("数据库更新结果:" + result);
        return ResultUtil.success();
    }


}
