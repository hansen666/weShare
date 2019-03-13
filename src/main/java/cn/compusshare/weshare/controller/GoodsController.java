package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.repository.RequestBody.PublishGoodsRequest;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/publish")
    public ResultResponse publish(@RequestBody PublishGoodsRequest publishGoodsRequest){
        logger.info("GoodsController.publish(),入参：userId={}，goodsName={},lable={},picUrl={},description={},price={}," +
                "phone={},longitude={},latitude={}",publishGoodsRequest.getUserId(),publishGoodsRequest.getGoodsName(),
                publishGoodsRequest.getLable(),publishGoodsRequest.getPicUrl(),publishGoodsRequest.getDescription(),
                publishGoodsRequest.getPrice(),publishGoodsRequest.getPhone(),publishGoodsRequest.getLongitude(),
                publishGoodsRequest.getLatitude());
        return goodsService.publishGoods(publishGoodsRequest);
    }
}
