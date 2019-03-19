package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/publish")
    public ResultResponse publish(@RequestHeader String token, @RequestBody GoodsRequest goodsRequest) {
        logger.info("GoodsController.publish(),入参：token={}，goodsName={},label={},picUrl={},description={},price={}," +
                        "phone={},longitude={},latitude={}", token, goodsRequest.getGoodsName(),
                goodsRequest.getLabel(), goodsRequest.getPicUrl(), goodsRequest.getDescription(),
                goodsRequest.getPrice(), goodsRequest.getPhone(), goodsRequest.getLongitude(),
                goodsRequest.getLatitude());
        return goodsService.publishGoods(token, goodsRequest);
    }

    @PostMapping("/want")
    public ResultResponse want(@RequestHeader String token, @RequestBody GoodsRequest goodsRequest) {
        logger.info("GoodsController.publish(),入参：token={}，goodsName={},label={},picUrl={},description={},price={}," +
                        "phone={},longitude={},latitude={}", token, goodsRequest.getGoodsName(),
                goodsRequest.getLabel(), goodsRequest.getPicUrl(), goodsRequest.getDescription(),
                goodsRequest.getPrice(), goodsRequest.getPhone(), goodsRequest.getLongitude(),
                goodsRequest.getLatitude());
        return goodsService.wantGoods(token, goodsRequest);
    }

    /**
     * 获取卖出的物品
     *
     * @param token
     * @return
     */
    @GetMapping("/sold")
    public ResultResponse sold(@RequestHeader String token, @RequestParam int currentPage) {
        logger.info("GoodsController.sold(),入参：token={},currentPage={}", token, currentPage);
        return ResultUtil.success(goodsService.getSoldGoods(token, currentPage));
    }

    /**
     * 获取收藏物品
     *
     * @param token
     * @return
     */
    @GetMapping("/collection")
    public ResultResponse collection(@RequestHeader String token, @RequestParam int currentPage) {
        logger.info("GoodsController.collection(),入参：token={},currentPage={}", token, currentPage);
        return ResultUtil.success(goodsService.collections(token, currentPage));
    }

    /**
     * 我的发布
     *
     * @param token
     * @param currentPage
     * @return
     */
    @GetMapping("myPublish")
    public ResultResponse myPublish(@RequestHeader String token, @RequestParam int currentPage) {
        logger.info("GoodsController.myPublish(),入参：token={},currentPage={}", token, currentPage);
        return ResultUtil.success(goodsService.myPublish(token, currentPage));
    }


    @GetMapping("/wishWall")
    public ResultResponse wishWall(@RequestHeader String token, @RequestParam int currentPage) {
        logger.info("GoodsController.wishWall(),入参:token={},currentPage={}", token, currentPage);
        return goodsService.wishWall(token, currentPage);
    }

}
