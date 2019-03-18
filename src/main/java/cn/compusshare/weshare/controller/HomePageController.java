package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.ShowGoodsRequest;
import cn.compusshare.weshare.service.HomePageService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homePage")
public class HomePageController {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private HomePageService homePageService;

    @GetMapping("/selectSchool1")
    public ResultResponse selectSchool1() {
        logger.info("HomePageController.selectSchool1(),入参：null");
        return ResultUtil.success(homePageService.selectAllSchool());
    }

    @GetMapping("/selectSchool2")
    public ResultResponse selectSchool2() {
        logger.info("HomePageController.selectSchool2(),入参：null");
        return ResultUtil.success(homePageService.allSchoolName());
    }

    @GetMapping("/showGoods")
    public ResultResponse showGoods(@RequestHeader String token, @RequestBody ShowGoodsRequest showGoodsRequest) {
        logger.info("HomePageController.showGoods(),入参:token={},pageIndex={},label={},keyword={}", token,
                showGoodsRequest.getPageIndex(), showGoodsRequest.getLabel(), showGoodsRequest.getKeyword());
        return homePageService.showGoods(token, showGoodsRequest.getPageIndex(), showGoodsRequest.getLabel(),
                showGoodsRequest.getKeyword());
    }

}
