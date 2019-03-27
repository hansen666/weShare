package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.AddUserRequest;
import cn.compusshare.weshare.repository.entity.User;
import cn.compusshare.weshare.service.UserService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * @Author: LZing
 * @Date: 2019/3/6
 * 用户管理Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

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
        logger.info("UserController.addUser(),入参:token={},schoolName={},nickName={},avatarUrl={}",
                token, addUserRequest.getSchoolName(), addUserRequest.getNickname(), addUserRequest.getAvatarUrl());
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
        logger.info("UserController.modify(),入参：token={}{}", token, user.toStringSelective());
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
    @GetMapping("information")
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
}
