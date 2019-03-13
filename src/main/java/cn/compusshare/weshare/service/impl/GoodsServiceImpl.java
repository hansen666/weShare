package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.repository.RequestBody.PublishGoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private PublishGoodsMapper publishGoodsMapper;

    @Override
    public ResultResponse publishGoods(PublishGoodsRequest publishGoodsRequest) {
        Date date = new Date();
        PublishGoods publishGoods = PublishGoods.builder()
                .publisherId(publishGoodsRequest.getUserId())
                .name(publishGoodsRequest.getGoodsName())
                .label(publishGoodsRequest.getLable())
                .description(publishGoodsRequest.getDescription())
                .price(publishGoodsRequest.getPrice())
                .picUrl(publishGoodsRequest.getPicUrl())
                .phone(publishGoodsRequest.getPhone())
                .longitude(publishGoodsRequest.getLongitude())
                .latitude(publishGoodsRequest.getLatitude())
                .browseCount(0)
                .status((byte)0)
                .pubTime(date)
                .build();

        try {
            publishGoodsMapper.insertSelective(publishGoods);
        } catch (Exception e){
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success();
    }
}
