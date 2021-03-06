package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.RequestBody.ImageRequest;
import cn.compusshare.weshare.repository.RequestBody.ShowGoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.WantGoods;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;


    /**
     * 发布物品
     *
     * @param token
     * @param goodsRequest
     * @return
     */
    @PostMapping("/publish")
    public ResultResponse publish(@RequestHeader String token, @RequestBody GoodsRequest goodsRequest) {
        logger.info("GoodsController.publish(),入参：token={}，goodsRequest={}", token, goodsRequest.toString());
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
        logger.info("GoodsController.publish(),入参：token={}，goodsRequest={}", token, goodsRequest.toString());
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

    /**
     * 删除我的卖出的物品记录
     *
     * @param token
     * @param data
     * @return
     */
    @DeleteMapping("/removeSoldGoods")
    public ResultResponse removeSoldGoods(@RequestHeader String token, @RequestBody Map<String, Integer[]> data) {
        logger.info("GoodsController.removeSoldGoods(),入参：token={},goodsID={}", token, data.get("goodsID"));
        return goodsService.removeSoldGoods(data.get("goodsID"));
    }

    /**
     * 物品是否被收藏
     *
     * @param token
     * @param goodsID
     * @return
     */
    @GetMapping("isGoodsCollected")
    public ResultResponse isGoodsCollected(@RequestHeader String token, @RequestParam int goodsID) {
        logger.info("GoodsController.isGoodsCollected(),入参：token={},goodsID={}", token, goodsID);
        return ResultUtil.success(goodsService.isGoodsCollected(token, goodsID));
    }

    /**
     * 收藏操作
     *
     * @param token
     * @param goodsID
     * @return
     */
    @PostMapping("/collect")
    public ResultResponse collect(@RequestHeader String token, @RequestParam int goodsID) {
        logger.info("GoodsController.collect(),入参：token={},goodsID={}", token, goodsID);
        return goodsService.collect(token, goodsID);
    }

    /**
     * 取消收藏操作
     *
     * @param token
     * @param data
     * @return
     */
    @DeleteMapping("/cancelCollection")
    public ResultResponse cancelCollection(@RequestHeader String token, @RequestBody Map<String, Integer[]> data) {
        logger.info("GoodsController.cancelCollection(),入参：token={},goodsID={}", token, data.get("goodsID"));
        return goodsService.cancelCollection(token, data.get("goodsID"));
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
     *
     * @param token
     * @return
     */
    @GetMapping("myPublish")
    public ResultResponse myPublish(@RequestHeader String token) {
        logger.info("GoodsController.myPublish(),入参：token={}", token);
        return ResultUtil.success(goodsService.myPublish(token));
    }

    /**
     * 修改我的发布
     *
     * @param token
     * @param publishGoods
     * @return
     */
    @PostMapping("/modifyMyPublish")
    public ResultResponse modifyMyPublish(@RequestHeader String token, @RequestBody PublishGoods publishGoods) {
        logger.info("GoodsController.modifyMyPublish(),入参：token={}, publishGoods={}", token, publishGoods.toString());
        return goodsService.myPublishModify(token, publishGoods);
    }

    /**
     * 根据ID删除我的发布
     *
     * @param token
     * @param data
     * @return
     */
    @DeleteMapping("/removePublish")
    public ResultResponse removePublish(@RequestHeader String token, @RequestBody Map<String, Integer[]> data) {
        logger.info("GoodsController.removePublish(),入参：token={},goodsID={}", token, data.get("goodsID"));
        return goodsService.removePublish(data.get("goodsID"));
    }

    /**
     * 我的求购
     *
     * @param token
     * @return
     */
    @GetMapping("myWanted")
    public ResultResponse myWanted(@RequestHeader String token) {
        logger.info("GoodsController.myWanted(),入参：token={}", token);
        return ResultUtil.success(goodsService.myWanted(token));
    }


    @PostMapping("/modifyMyWanted")
    public ResultResponse modifyMyWanted(@RequestHeader String token, @RequestBody WantGoods wantGoods) {
        logger.info("GoodsController.modifyMyWanted(),入参：token={}, publishGoods={}", token, wantGoods.toString());
        return goodsService.myWantedModify(token, wantGoods);
    }

    /**
     * 根据ID删除我的求购
     *
     * @param token
     * @param data
     * @return
     */
    @DeleteMapping("/removeWanted")
    public ResultResponse removeWanted(@RequestHeader String token, @RequestParam Map<String, Integer[]> data) {
        logger.info("GoodsController.removeWanted(),入参：token={},goodsID={}", token, data.get("goodsID"));
        return goodsService.removeWanted(data.get("goodsID"));
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
        logger.info("GoodsController.showHomeGoods(),入参:token={}, showGoodsRequest={}", token, showGoodsRequest.toString());
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
    public ResultResponse showDetail(@RequestParam Integer id) {
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
    public ResultResponse upload(@RequestHeader String token, ImageRequest imageRequest) {
        logger.info("ImageController.imageUpload(),传入图片={}", imageRequest.getFile().getOriginalFilename());
        return goodsService.uploadImage(token, imageRequest.getFile(), imageRequest.getId(), imageRequest.getFilePath());
    }

    /**
     * 交易完成接口
     *
     * @param token
     * @param goodsID
     * @return
     */
    @PostMapping("/dealCompleted")
    public ResultResponse dealCompleted(@RequestHeader String token, @RequestParam int goodsID) {
        logger.info("GoodsController.dealCompleted(),入参：token={},goodsID={}", token, goodsID);
        return goodsService.dealComplete(token, goodsID);
    }

    /**
     * 发送评论
     *
     * @param token
     * @param request
     * @return
     */
    @PostMapping("/sendComment")
    public ResultResponse sendComment(@RequestHeader String token, @RequestBody Map<String, Object> request) {
        logger.info("GoodsController.sendComment(),入参：token={},request={}", token, request.toString());
        return goodsService.sendComment(token, request);
    }

    /**
     * 获取评论
     *
     * @param goodsID
     * @return
     */
    @GetMapping("/getComments")
    public ResultResponse getComments(@RequestParam int goodsID) {
        logger.info("GoodsController.getComments(),入参：goodsID={}", goodsID);
        return ResultUtil.success(goodsService.getComments(goodsID));
    }

    /**
     * 删除图片
     *
     * @param token
     * @param request
     * @return
     */
    @PostMapping("/deleteImage")
    public ResultResponse deleteImage(@RequestHeader String token, @RequestBody Map<String, String> request) {
        logger.info("GoodController.deleteImage(),入参: token={}, request={}", token, request.toString());
        return goodsService.deleteImage(Integer.valueOf(request.get("id")), request.get("imageName"), request.get("method"));
    }

}
