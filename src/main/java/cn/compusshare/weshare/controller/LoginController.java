package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LZing
 * @Date: 2019/3/8
 * 登陆与注册controller
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private LoginService loginService;

    @GetMapping("/getToken")
    public ResultResponse  getToken(@RequestParam String code) throws Exception{
        logger.info("LoginController.getToken(),入参：code={}",code);
        return ResultUtil.success(loginService.getToken(code));
    }
}
