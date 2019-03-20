package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.RequestBody.ImageRequest;
import cn.compusshare.weshare.repository.RequestBody.ShowGoodsRequest;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private Environment environment;


    /**
     * 发布物品
     *
     * @param token
     * @param goodsRequest
     * @return
     */
    @PostMapping("/publish")
    public ResultResponse publish(@RequestHeader String token, @RequestBody GoodsRequest goodsRequest) {
        logger.info("GoodsController.publish(),入参：token={}，goodsName={},label={},picUrl={},description={},price={}," +
                        "phone={},longitude={},latitude={}", token, goodsRequest.getGoodsName(),
                goodsRequest.getLabel(), goodsRequest.getPicUrl(), goodsRequest.getDescription(),
                goodsRequest.getPrice(), goodsRequest.getPhone(), goodsRequest.getLongitude(),
                goodsRequest.getLatitude());
        return goodsService.publishGoods(token, goodsRequest);
    }


    /**
     * 求购物品
     *
     * @param token
     * @param goodsRequest
     * @return
     */
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
     * @param token
     * @param currentPage
     * @return
     */
    @GetMapping("myPublish")
    public ResultResponse myPublish(@RequestHeader String token, @RequestParam int currentPage) {
        logger.info("GoodsController.myPublish(),入参：token={},currentPage={}", token, currentPage);
        return ResultUtil.success(goodsService.myPublish(token, currentPage));
    }
    /**
     * 主页显示物品
     *
     * @param token
     * @param showGoodsRequest
     * @return
     */
    @GetMapping("/showHomeGoods")
    public ResultResponse showHomeGoods(@RequestHeader String token, ShowGoodsRequest showGoodsRequest) {
        logger.info("HomePageController.showHomeGoods(),入参:token={},currentPage={},label={},keyword={}", token,
                showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(), showGoodsRequest.getKeyword());
        return goodsService.showHomeGoods(token, showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(),
                showGoodsRequest.getKeyword());
    }

    /**
     * 主页物品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/showHomeDetail")
    public ResultResponse showHomeDetail(@RequestParam Integer id) {
        logger.info("GoodsController.showHomeDetail(),入参:id={}", id);
        return goodsService.showHomeDetail(id);
    }

    /**
     * 浏览心愿墙物品
     *
     * @param token
     * @param currentPage
     * @return
     */
    @GetMapping("/showWishWall")
    public ResultResponse wishWall(@RequestHeader String token, @RequestParam int currentPage, @RequestParam Byte label) {
        logger.info("GoodsController.showWishWall(),入参:token={},currentPage={}, label={}", token, currentPage, label);
        return goodsService.showWishWall(token, currentPage, label);
    }

    /**
     * 心愿墙物品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/showWishDetail")
    public ResultResponse showDetail(@RequestParam Integer id){
        logger.info("GoodsController.showWishDetail(),入参:id={}", id);
        return goodsService.showWishDetail(id);
    }

    /**
     * 图片上传
     *
     * @param imageRequest
     * @return
     */
    @PostMapping("/imageUpload")
    public ResultResponse upload(ImageRequest imageRequest) {
        String savePath = environment.getProperty("image.path") + imageRequest.getFilePath()+"\\";
        logger.info("ImageController.imageUpload(),传入图片={}", imageRequest.getFile().getOriginalFilename());
        return goodsService.uploadImage(imageRequest.getFile(), imageRequest.getId(), savePath);
    }
}
