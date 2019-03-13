package cn.compusshare.weshare.controller;

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

    @PostMapping("publish")
    public ResultResponse publish(@RequestBody){

        return goodsService.publishGoods();
    }
}
