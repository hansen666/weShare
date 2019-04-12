package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.service.AdminService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/admin")
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @GetMapping("/verifiedUser")
    public ResultResponse verifiedUser(@RequestHeader String token, @RequestParam int type) {
        logger.info("AdminController.verifiedUser(),入参：token={},type={}", token, type);
        return adminService.userQuery(type);
    }
}
