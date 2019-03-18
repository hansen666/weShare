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

import java.util.Map;

/**
 * @Author: LZing
 * @Date: 2019/3/6
 * 用户管理Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private UserService userService;

    /**
     * 添加用户
     * @param token
     * @param addUserRequest
     * @return
     */
    @PostMapping("/addUser")
    public ResultResponse addUser(@RequestHeader String token,@RequestBody AddUserRequest addUserRequest){
        logger.info("UserController.addUser(),入参:token={},schoolName={},nickName={},avatarUrl={}",
                token,addUserRequest.getSchoolName(),addUserRequest.getNickname(),addUserRequest.getAvatarUrl());
        return userService.addUser(token,addUserRequest);
    }

    /**
     * 修改资料
     * @param token
     * @param user
     * @return
     */
    @PostMapping("/modify")
    public ResultResponse modify(@RequestHeader String token, @RequestBody User user) {
        logger.info("UserController.modify(),入参：" + user.toStringSelective());
        return userService.modify(token, user);
    }

    @GetMapping("/test")
    public ResultResponse test(@RequestBody Map<String,Object> requestBody) {
        String schoolName= ((String) requestBody.get("schoolName"));
        int ID = ((int) requestBody.get("id"));
        return ResultUtil.success(requestBody);
    }
}
