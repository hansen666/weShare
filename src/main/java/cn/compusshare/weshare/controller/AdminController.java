package cn.compusshare.weshare.controller;

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
     * @param session
     * @param param
     * @return
     */
    @PostMapping("/login")
    public ResultResponse login(HttpSession session, @RequestBody Map<String,String> param) {
        logger.info("AdminController.login, 入参：session = {}， param = {}", session.getId(), param.toString());
        return adminService.login(param.get("account"), param.get("password"), session);
    }

    /**
     * 退出登录
     * @param account
     * @param token
     * @return
     */
    @GetMapping("/logout")
    public ResultResponse logout(@RequestHeader String account, @RequestHeader String token) {
        logger.info("AdminController.logout, 入参：account = {}, token = {}", account, token);
        return adminService.logout(account, token);
    }


    @GetMapping("/verifiedUser")
    public ResultResponse verifiedUser(@RequestHeader String token, @RequestHeader String account, @RequestParam int type) {
        logger.info("AdminController.verifiedUser(),入参：token={},type={}", token, type);
        return adminService.userQuery(type);
    }
}
