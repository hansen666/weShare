package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.AddUserRequest;
import cn.compusshare.weshare.repository.entity.User;
import cn.compusshare.weshare.service.UserService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


/**
 * @Author: LZing
 * @Date: 2019/3/6
 * 用户管理Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 添加用户
     *
     * @param token
     * @param addUserRequest
     * @return
     */
    @PostMapping("/addUser")
    public ResultResponse addUser(@RequestHeader String token, @RequestBody AddUserRequest addUserRequest) {
        logger.info("UserController.addUser(),入参:token={}, addUserRequest={}", token, addUserRequest.toString());
        return userService.addUser(token, addUserRequest);
    }

    /**
     * 修改资料
     *
     * @param token
     * @param user
     * @return
     */
    @PostMapping("/modify")
    public ResultResponse modify(@RequestHeader String token, @RequestBody User user) {
        logger.info("UserController.modify(),入参：token={},user={}", token, user.toString());
        return userService.modify(token, user);
    }

    /**
     * 获取认证类型
     *
     * @param token
     * @return
     */
    @GetMapping("/identifiedType")
    public ResultResponse queryIdentifiedType(@RequestHeader String token) {
        logger.info("UserController.queryIdentifiedType(),入参：token={}", token);
        return ResultUtil.success(userService.queryIdentifiedType(token));
    }

    /**
     * 获取用户基本信息
     *
     * @param token
     * @return
     */
    @GetMapping("/information")
    public ResultResponse userInformation(@RequestHeader String token) {
        logger.info("UserController.userInformation(),入参：token={}", token);
        return ResultUtil.success(userService.information(token));
    }

    /**
     * 学籍认证
     *
     * @param token
     * @param onlineCode
     * @return
     */
    @GetMapping("/studentCertify")
    public ResultResponse studentCertify(@RequestHeader String token, @RequestParam String onlineCode) {
        logger.info("UserController.studentCertify(),入参:token={}, onlineCode={}", token, onlineCode);
        try {
            return userService.studentCertify(token, onlineCode);
        } catch (IOException e) {
            logger.info(e.getMessage());
            return ResultUtil.fail(-1, "网页加载超时");
        }
    }

    /**
     * 根据用户ID获取头像url
     * @param userId
     * @return
     */
    @GetMapping("/avatarUrl")
    public ResultResponse avatarUrl(@RequestParam String userId) {
        logger.info("UserController.avatarUrl(),入参:userId={}", userId);
        return ResultUtil.success(userService.getAvatarUrlById(userId));
    }

    /**
     * 根据token获取头像url
     * @param token
     * @return
     */
    @GetMapping("/myAvatarUrl")
    public ResultResponse myAvatarUrl(@RequestHeader String token) {
        logger.info("UserController.myAvatarUrl(),入参:token={}", token);
        return ResultUtil.success(userService.getAvatarUrlByToken(token));
    }

    /**
     * 发送反馈
     * @param token
     * @param param
     * @return
     */
    @PostMapping("/sendFeedback")
    public ResultResponse seedFeedback(@RequestHeader String token, @RequestBody Map<String, String> param) {
        logger.info("UserController.sendFeedback(),入参：token={},param={}", token, param.toString());
        return userService.sendFeedback(token, param.get("content"));
    }


}
