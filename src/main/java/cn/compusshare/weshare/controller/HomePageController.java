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

    /**
     * 选择所有学校，按省分开
     *
     * @return
     */
    @GetMapping("/selectSchool1")
    public ResultResponse selectSchool1() {
        logger.info("HomePageController.selectSchool1(),入参：null");
        return ResultUtil.success(homePageService.selectAllSchool());
    }

    /**
     * 选择所有学校名
     *
     * @return
     */
    @GetMapping("/selectSchool2")
    public ResultResponse selectSchool2() {
        logger.info("HomePageController.selectSchool2(),入参：null");
        return ResultUtil.success(homePageService.allSchoolName());
    }

    /**
     * 主页显示物品
     *
     * @param token
     * @param showGoodsRequest
     * @return
     */
    @GetMapping("/showGoods")
    public ResultResponse showGoods(@RequestHeader String token, ShowGoodsRequest showGoodsRequest) {
        logger.info("HomePageController.showGoods(),入参:token={},currentPage={},label={},keyword={}", token,
                showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(), showGoodsRequest.getKeyword());
        return homePageService.showGoods(token, showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(),
                showGoodsRequest.getKeyword());
    }

    /**
     * 主页物品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/showDetail")
    public ResultResponse showDetail(@RequestParam Integer id) {
        logger.info("GoodsController.showDetail(),入参:id={}", id);
        return homePageService.showDetail(id);
    }
}
