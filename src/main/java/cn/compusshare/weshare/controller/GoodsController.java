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

import java.util.Date;

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
    public ResultResponse sold(@RequestHeader String token) {
        logger.info("GoodsController.sold(),入参：token={}", token);
        return ResultUtil.success(goodsService.getSoldGoods(token));
    }

    @GetMapping("/collect")
    public ResultResponse collect(@RequestHeader String token,@RequestParam int goodsID) {
        logger.info("GoodsController.collect(),入参：token={},goodsID={}", token, goodsID);
        return goodsService.collect(token,goodsID);
    }

    /**
     * 获取收藏物品
     *
     * @param token
     * @return
     */
    @GetMapping("/collections")
    public ResultResponse collections(@RequestHeader String token) {
        logger.info("GoodsController.collection(),入参：token={}", token);
        return ResultUtil.success(goodsService.collections(token));
    }

    /**
     * 我的发布
     * @param token
     * @return
     */
    @GetMapping("myPublish")
    public ResultResponse myPublish(@RequestHeader String token) {
        logger.info("GoodsController.myPublish(),入参：token={}", token);
        return ResultUtil.success(goodsService.myPublish(token));
    }

    /**
     * 我的求购
     * @param token
     * @return
     */
    @GetMapping("myWanted")
    public ResultResponse myWanted(@RequestHeader String token) {
        logger.info("GoodsController.myWanted(),入参：token={}", token);
        return ResultUtil.success(goodsService.myWanted(token));
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
        logger.info("GoodsController.showHomeGoods(),入参:token={},currentPage={},label={},keyword={},pubTime={}", token,
                showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(), showGoodsRequest.getKeyword(),
                showGoodsRequest.getCurrentPage());
        return goodsService.showHomeGoods(token, showGoodsRequest.getCurrentPage(), showGoodsRequest.getLabel(),
                showGoodsRequest.getKeyword(), showGoodsRequest.getCurrentTime());
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
    public ResultResponse wishWall(@RequestHeader String token, @RequestParam int currentPage,
                                   @RequestParam Byte label, @RequestParam String currentTime) {
        logger.info("GoodsController.showWishWall(),入参:token={},currentPage={}, label={}, pubTime={}", token,
                currentPage, label, currentTime);
        return goodsService.showWishWall(token, currentPage, label, currentTime);
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
