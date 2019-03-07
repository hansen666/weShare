package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.service.HomePageService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/homePage")
public class HomePageController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private HomePageService homePageService;

    @GetMapping("/selectSchool1")
    public ResultResponse selectSchool1(){
        logger.info("HomePageController.selectSchool1(),入参：null");
        return ResultUtil.success(homePageService.selectAllSchool());
    }

    @GetMapping("/selectSchool2")
    public ResultResponse selectSchool2(){
        logger.info("HomePageController.selectSchool2(),入参：null");
        return ResultUtil.success(homePageService.allSchoolName());
    }

}
