package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.AdminGoodsRequest;
import cn.compusshare.weshare.service.AdminService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    /**
     * 登录
     *
     * @param session
     * @param param
     * @return
     */
    @PostMapping("/login")
    public ResultResponse login(HttpSession session, @RequestBody Map<String, String> param) {
        logger.info("AdminController.login, 入参：session={}， param={}", session.getId(), param.toString());
        return adminService.login(param.get("account"), param.get("password"), session);
    }

    /**
     * 退出登录
     *
     * @param account
     * @param token
     * @return
     */
    @GetMapping("/logout")
    public ResultResponse logout(@RequestHeader String account, @RequestHeader String token) {
        logger.info("AdminController.logout, 入参：account={}, token={}", account, token);
        return adminService.logout(account, token);
    }

    /**
     * 获取用户列表
     *
     * @param account
     * @param token
     * @param type
     * @param currentPage
     * @return
     */
    @GetMapping("/getUserList")
    public ResultResponse getUserList(@RequestHeader String account, @RequestHeader String token, @RequestParam String nickname, @RequestParam Integer type, @RequestParam Integer currentPage) {
        logger.info("AdminController.getUserList(),入参：account={}, token={}, nickname={}, type={}, currentPage={}", account, token, nickname, type, currentPage);
        return adminService.userQuery(nickname, type, currentPage);
    }

    /**
     * 每日发布物品数量
     *
     * @param account
     * @param token
     * @param year
     * @return
     */
    @GetMapping("/monthlyPublishGoodsQuantity")
    public ResultResponse monthlyPublishGoodsQuantity(@RequestHeader String account, @RequestHeader String token,
                                                      @RequestParam Integer year, @RequestParam Integer flag) {
        logger.info("AdminController.monthlyPublishGoodsQuantity(),入参：account={},token={},year={},flag={}",
                account, token, year, flag);
        return adminService.monthlyGoodsQuantity(year, flag);
    }

    /**
     * 每日物品发布量统计
     *
     * @param account
     * @param token
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/dailyPublishGoodsQuantity")
    public ResultResponse dailyPublishGoodsQuantity(@RequestHeader String account, @RequestHeader String token,
                                                    @RequestParam Integer year, @RequestParam Integer month, @RequestParam Integer flag) {
        logger.info("AdminController.dailyPublishGoodsQuantity(),入参：account={},token={},year={},month={},flag={}",
                account, token, year, month, flag);
        return adminService.dailyGoodsQuantity(year, month, flag);
    }

    /**
     * 每月用户注册统计
     *
     * @param account
     * @param token
     * @param year
     * @return
     */
    @GetMapping("/monthlyUserQuantity")
    public ResultResponse monthlyUserQuantity(@RequestHeader String account, @RequestHeader String token, @RequestParam Integer year) {
        logger.info("AdminController.monthlyUserQuantity(),入参：account={},token={},year={}", account, token, year);
        return adminService.monthlyUserQuantity(year);
    }

    /**
     * 每日用户注册统计
     *
     * @param account
     * @param token
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/dailyUserQuantity")
    public ResultResponse dailyUserQuantity(@RequestHeader String account, @RequestHeader String token,
                                            @RequestParam Integer year, @RequestParam Integer month) {
        logger.info("AdminController.dailyUserQuantity(),入参：account={},token={},year={},month={}",
                account, token, year, month);
        return adminService.dailyUserQuantity(year, month);
    }

    /**
     * 查询审核未通过的物品
     *
     * @param account
     * @param token
     * @param currentPage
     * @param flag        标识，从发布表或求购表查
     * @return
     */
    @GetMapping("/auditFailGoods")
    public ResultResponse auditFailGoods(@RequestHeader String account,
                                         @RequestHeader String token, @RequestParam Integer currentPage, @RequestParam Integer flag) {
        logger.info("AdminController.auditFailGoods(),入参：account={},token={},currentPage={},flag={}",
                account, token, currentPage, flag);
        return adminService.auditFailGoods(currentPage, flag);
    }

    /**
     * 修改物品的审核状态
     *
     * @param account
     * @param token
     * @param param
     * @return
     */
    @PostMapping("/changeGoodsStatus")
    public ResultResponse changeGoodsStatus(@RequestHeader String account, @RequestHeader String token,
                                            @RequestBody Map<String, Object> param) {
        logger.info("AdminController.changeGoodsStatus(),入参：account={},token={}，param={}", account, token, param.toString());
        return adminService.changeGoodsStatus(((Integer) param.get("id")), (Byte) param.get("status"), (Integer) param.get("flag"));
    }

    /**
     * 每月成交量统计
     *
     * @param account
     * @param token
     * @param year
     * @return
     */
    @GetMapping("/monthlyGoodsTransactionQuantity")
    public ResultResponse monthlyGoodsTransactionQuantity(@RequestHeader String account, @RequestHeader String token,
                                                          @RequestParam Integer year) {
        logger.info("AdminController.monthlyGoodsTransactionQuantity(),入参：account={},token={},year={}", account, token, year);
        return adminService.monthlyGoodsTransactionQuantity(year);
    }

    /**
     * 每日成量统计
     *
     * @param account
     * @param token
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/dailyGoodsTransactionQuantity")
    public ResultResponse dailyGoodsTransactionQuantity(@RequestHeader String account, @RequestHeader String token,
                                                        @RequestParam Integer year, @RequestParam Integer month) {
        logger.info("AdminController.dailyGoodsTransactionQuantity(),入参：account={},token={},year={},month={}",
                account, token, year, month);
        return adminService.dailyGoodsTransactionQuantity(year, month);
    }

    /**
     * 用户卖出的
     *
     * @param id
     * @return
     */
    @GetMapping("/userSold")
    public ResultResponse userSold(@RequestHeader String account, @RequestHeader String token, @RequestParam String id) {
        logger.info("AdminController.userSold(), 入参:account={},token={},id={}", account, token, id);
        return adminService.userSold(id);
    }

    /**
     * 用户求购
     *
     * @param id
     * @return
     */
    @GetMapping("/userWanted")
    public ResultResponse userWanted(@RequestHeader String account, @RequestHeader String token, @RequestParam String id) {
        logger.info("AdminController.userWanted(), 入参:account={},token={},id={}", account, token, id);
        return adminService.userWanted(id);
    }
    @PostMapping("/goodsRecord")
    public ResultResponse goodsRecord(@RequestHeader String account, @RequestHeader String token, @RequestBody AdminGoodsRequest request) {
        logger.info("AdminController.goodsRecord(), 入参:account={},token={},requestParam={}", account, token, request.toString());
        return adminService.goodsRecord(request);
    }


    /**
     * 用户发布
     *
     * @param id
     * @return
     */
    @GetMapping("/userPublish")
    public ResultResponse userPublish(@RequestHeader String account, @RequestHeader String token, @RequestParam String id) {
        logger.info("AdminController.userPublish(), 入参:account={},token={},id={}", account, token, id);
        return adminService.userPublish(id);
    }

    /**
     * 用户收藏
     *
     * @param id
     * @return
     */
    @GetMapping("/userCollections")
    public ResultResponse userCollections(@RequestHeader String account, @RequestHeader String token, @RequestParam String id) {
        logger.info("AdminController.userCollections(), 入参:account={},token={},id={}", account, token, id);
        return adminService.userCollections(id);
    }

    /**
     * 获取用户详细信息
     *
     * @param account
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/userFullInfo")
    public ResultResponse userFullInfo(@RequestHeader String account, @RequestHeader String token, @RequestParam String id) {
        logger.info("AdminController.userFullInfo(), 入参:account={}, token={}, id={}", account, token, id);
        return adminService.getUserFullInfo(id);
    }

    /**
     * 查询物品详情
     * @param account
     * @param token
     * @param id
     * @param flag
     * @return
     */
    @GetMapping("/goodsDetail")
    public ResultResponse goodsDetail(@RequestHeader String account, @RequestHeader String token, @RequestParam Integer id, @RequestParam Integer flag) {
        logger.info("AdminController.goodsDetail(),入参：account={},token={},id={},flag={}", account, token, id, flag);
        return adminService.goodsDetail(id, flag);
    }
}
