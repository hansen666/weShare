package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.RequestBody.AdminGoodsRequest;
import cn.compusshare.weshare.repository.entity.Admin;
import cn.compusshare.weshare.repository.entity.User;
import cn.compusshare.weshare.repository.mapper.*;
import cn.compusshare.weshare.service.AdminService;
import cn.compusshare.weshare.service.GoodsService;
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
import java.text.SimpleDateFormat;
import java.util.*;
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
    private GoodsService goodsService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PublishGoodsMapper publishGoodsMapper;

    @Autowired
    private WantGoodsMapper wantGoodsMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private Environment environment;

    /**
     * 用户查询
     *
     * @param type
     * @param currentPage
     * @return
     */
    @Override
    public ResultResponse userQuery(String nickname, Integer type, Integer currentPage) {
        try {
            List<Map<String, Object>> userList = userMapper.selectUserByType(nickname, type, currentPage * 7);
            int count = userMapper.userQueryCount(nickname, type);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userList.forEach(t -> t.put("updateTime", df.format(t.get("updateTime"))));
            HashMap<String, Object> result = new HashMap<>();
            result.put("count", count);
            result.put("userList", userList);
            return ResultUtil.success(result);
        } catch (Exception e) {
            logger.info("userQuery用户数据库查询错误" + e.getMessage());
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
        if (admin == null) {
            return ResultUtil.fail(Common.ACCOUNT_OR_PASSWORD_ERROR, Common.ACCOUNT_OR_PASSWORD_ERROR_MSG);
        }

        //md5加密
        password = DigestUtils.md5Hex(password);
        //密码不相等
        if (!password.equals(admin.getPassword())) {
            return ResultUtil.fail(Common.ACCOUNT_OR_PASSWORD_ERROR, Common.ACCOUNT_OR_PASSWORD_ERROR_MSG);
        }

        String token = cacheService.getString(account);

        if (CommonUtil.isEmpty(token)) {
            token = refreshToken(account, environment.getProperty("adminTokenKey"), session.getId());
            logger.info(account + "登录成功");
            Map<String, String> result = new HashMap<>(1);
            result.put("token", token);
            return ResultUtil.success(result);
        } else {  //如果缓存中有token，说明之前已有人登录,把另一账号挤下线
            token = refreshToken(account, environment.getProperty("adminTokenKey"), session.getId());
            logger.info(account + "登录成功，另一地点登录的账号被迫下线");
            Map<String, String> result = new HashMap<>(1);
            result.put("token", token);
            return ResultUtil.success(Common.LOGIN_ALREADY, Common.LOGIN_ALREADY_MSG, result);
        }
    }

    /**
     * 刷新token
     *
     * @param account
     * @param key
     * @param claim
     * @return
     */
    private String refreshToken(String account, String key, String claim) {
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
        boolean redisResult = cacheService.del(account);
        logger.info(account + "退出登录, token缓存删除：" + redisResult);
        return ResultUtil.success();
    }

    /**
     * 统计某一年中每月发布的物品数量
     *
     * @param year
     * @param flag
     * @return
     */
    @Override
    public ResultResponse monthlyGoodsQuantity(int year, int flag) {
        if (year < 0) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }

        List<Map<String, Object>> result;
        if (flag == 0) {
            //在publishGoods表中查
            result = publishGoodsMapper.monthlyQuantity(year);
        } else {
            //在wantGoods表中查
            result = wantGoodsMapper.monthlyQuantity(year);
        }
        fillMonth(result);
        return ResultUtil.success(result);
    }

    /**
     * 某月中每日的物品发布量
     *
     * @param year
     * @param month
     * @param flag
     * @return
     */
    @Override
    public ResultResponse dailyGoodsQuantity(int year, int month, int flag) {
        if (year < 0 || (month < 1 || month > 12)) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> result;
        if (flag == 0) {
            //在publishGoods表中查
            result = publishGoodsMapper.dailyQuantity(year, month);
        } else {
            //在wantGoods表中查
            result = wantGoodsMapper.dailyQuantity(year, month);
        }
        fillDay(result, year, month);
        return ResultUtil.success(result);
    }

    /**
     * 某年的用户注册数量
     *
     * @param year
     * @return
     */
    @Override
    public ResultResponse monthlyUserQuantity(int year) {
        if (year < 0) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = userMapper.monthlyQuantity(year);
        fillMonth(resultMap);
        return ResultUtil.success(resultMap);
    }

    /**
     * 某年某月的用户注册数量
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public ResultResponse dailyUserQuantity(int year, int month) {
        if (year < 0 || (month < 1 || month > 12)) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> resultMap = userMapper.dailyQuantity(year, month);
        fillDay(resultMap, year, month);
        return ResultUtil.success(resultMap);
    }

    /**
     * 查询审核未通过的物品
     *
     * @param currentPage 分页
     * @param flag        标识，发布的或求购的
     * @return
     */
    @Override
    public ResultResponse auditFailGoods(int currentPage, int flag) {
        List<Map<String, Object>> result;
        if (flag == 0) {
            result = publishGoodsMapper.auditFailGoods(currentPage * 0);
        } else {
            result = wantGoodsMapper.auditFailGoods(currentPage * 10);
        }
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return ResultUtil.success(result);
    }

    /**
     * 更新物品状态
     *
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
        } else {
            result = wantGoodsMapper.updateStatus(id, status);
        }
        logger.info("数据库更新结果:" + result);
        return ResultUtil.success();
    }

    /**
     * 填充月份
     *
     * @param result
     */
    private void fillMonth(List<Map<String, Object>> result) {
        fill(result, 12);
    }

    /**
     * 填充日期
     *
     * @param result
     * @param year
     * @param month
     */
    private void fillDay(List<Map<String, Object>> result, int year, int month) {
        //获取月的天数
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.getTimeInMillis();
        int day = calendar.getActualMaximum(Calendar.DATE);

        //填充
        fill(result, day);
    }

    /**
     * 填充函数
     *
     * @param result
     * @param total  根据total判断填充月份还是日期
     */
    private void fill(List<Map<String, Object>> result, int total) {
        //确定要填充的是月份还是日期
        String key = total > 12 ? "day" : "month";
        for (int i = 1; i <= total; i++) {
            // 填充标识
            int flag = 0;
            for (Map<String, Object> map : result) {
                //如果已经包含，直接退出
                if (((Integer) map.get(key)) == i) {
                    flag = 1;
                    break;
                }
            }
            // flag为0说明未包含，填充
            if (flag == 0) {
                Map<String, Object> newMap = new HashMap<>(2);
                newMap.put(key, i);
                newMap.put("count", 0);
                result.add(newMap);
            }
        }

        //排序
        result.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer) o1.get(key) - (Integer) o2.get(key);
            }
        });
    }

    /**
     * 每月成交量统计
     *
     * @param year
     * @return
     */
    @Override
    public ResultResponse monthlyGoodsTransactionQuantity(int year) {
        if (year < 0) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> result = transactionRecordMapper.monthlyQuantity(year);
        fillMonth(result);
        return ResultUtil.success(result);
    }

    /**
     * 每日成交量统计
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public ResultResponse dailyGoodsTransactionQuantity(int year, int month) {
        if (year < 0 || (month < 1 || month > 12)) {
            return ResultUtil.fail(Common.PARAM_INVALID, Common.PARAM_INVALID_MSG);
        }
        List<Map<String, Object>> result = transactionRecordMapper.dailyQuantity(year, month);
        fillDay(result, year, month);
        return ResultUtil.success(result);
    }

    /**
     * 用户卖出的
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse userSold(String id) {
        List<Integer> goodsIds = transactionRecordMapper.selectGoodsId(id);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情
        List<Map<String, Object>> soldGoodsList = new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String, Object> goods = publishGoodsMapper.selectSoldGoods(goodsId);
            if (goods != null) {
                goods.put("pubTime", CommonUtil.timeFromNow((Date) goods.get("pubTime")));
                goods.put("updateTime", CommonUtil.timeFromNow((Date) goods.get("updateTime")));
                soldGoodsList.add(goods);
            }
        }
        return ResultUtil.success(soldGoodsList);
    }

    /**
     * 用户求购的
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse userWanted(String id) {
        List<Map<String, Object>> result = wantGoodsMapper.selectMyWanted(id);
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return ResultUtil.success(result);
    }
    /**
     * 查询物品记录
     * @param request
     * @return
     */
    @Override
    public ResultResponse goodsRecord(AdminGoodsRequest request) {
        List<Map<String, Object>> result;
        int count;
        if (request.getFlag() == 1) {
            result = wantGoodsMapper.selectAdminGoods(request.getGoodsName(), request.getNickname(),
                    request.getLabel(), request.getStatus(), request.getStartDate(), request.getEndDate(),
                    request.getSchoolName(), request.getCurrentPage() * 7);
            count = wantGoodsMapper.selectAdminCount(request.getGoodsName(), request.getNickname(),
                    request.getLabel(), request.getStatus(), request.getStartDate(), request.getEndDate(),
                    request.getSchoolName());
        }else {
            result = publishGoodsMapper.selectAdminGoods(request.getGoodsName(), request.getNickname(),
                    request.getLabel(), request.getStatus(), request.getStartDate(), request.getEndDate(),
                    request.getSchoolName(), request.getCurrentPage() * 7);
            count = publishGoodsMapper.selectAdminCount(request.getGoodsName(), request.getNickname(),
                    request.getLabel(), request.getStatus(), request.getStartDate(), request.getEndDate(),
                    request.getSchoolName());
        }
        result.forEach(map -> map.put("pubTime",CommonUtil.timeFromNow((Date)map.get("pubTime"))));
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("count", count);
        resultMap.put("goodsList", result);
        return ResultUtil.success(resultMap);

    }


    /**
     * 用户发布的
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse userPublish(String id) {
        List<Map<String, Object>> result = publishGoodsMapper.selectMyPublish(id);
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return ResultUtil.success(result);
    }

    /**
     * 用户收藏的
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse userCollections(String id) {
        List<Integer> goodsIds = collectionMapper.selectGoodsId(id);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情,筛选审核中的物品
        List<Map<String, Object>> collectionGoodsList = new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String, Object> goods = publishGoodsMapper.selectCollection(goodsId);
            if (goods != null) {
                goods.put("pubTime", CommonUtil.timeFromNow((Date) goods.get("pubTime")));
                collectionGoodsList.add(goods);
            }
        }
        return ResultUtil.success(collectionGoodsList);
    }

    /**
     * 获取用户详细信息
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse getUserFullInfo(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        return ResultUtil.success(user);
    }

    /**
     * 查询物品详情
     * @param id
     * @param flag
     * @return
     */
    @Override
    public ResultResponse goodsDetail(int id, int flag) {
        Map<String, Object> resultMap = new HashMap<>(2);
        if (flag == 1) {
            Map<String, Object> goodsDetail = wantGoodsMapper.showGoodsDetail(id);
            goodsDetail.put("pubTime",CommonUtil.timeFromNow((Date)goodsDetail.get("pubTime")));
            //查评论
            List<Map<String, Object>> commentList = commentMapper.selectByGoodsID(id);
            commentList.forEach(comment -> {
                comment.put("pubTime",CommonUtil.timeFromNow((Date)comment.get("pubTime")));
                comment.remove("create_time");
            });
            resultMap.put("commentList", commentList);
            resultMap.put("goodsDetail", goodsDetail);
        } else {
            Map<String, Object> goodsDetail = publishGoodsMapper.showGoodsDetail(id);
            resultMap.put("goodsDetail", goodsDetail);
        }
        return ResultUtil.success(resultMap);
    }
}
