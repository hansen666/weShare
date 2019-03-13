package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.PublishGoodsRequest;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/publish")
    public ResultResponse publish(@RequestHeader String token,@RequestBody PublishGoodsRequest publishGoodsRequest){
        logger.info("GoodsController.publish(),入参：token={}，goodsName={},label={},picUrl={},description={},price={}," +
                "phone={},longitude={},latitude={}",token,publishGoodsRequest.getGoodsName(),
                publishGoodsRequest.getLabel(),publishGoodsRequest.getPicUrl(),publishGoodsRequest.getDescription(),
                publishGoodsRequest.getPrice(),publishGoodsRequest.getPhone(),publishGoodsRequest.getLongitude(),
                publishGoodsRequest.getLatitude());
        return goodsService.publishGoods(token,publishGoodsRequest);
    }
}
