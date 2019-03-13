package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.repository.RequestBody.PublishGoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.TransactionRecordMapper;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Date;

@Component
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;
    @Autowired
    private PublishGoodsMapper publishGoodsMapper;

    @Override
    public ResultResponse publishGoods(String token,PublishGoodsRequest publishGoodsRequest) {
        Date date = new Date();
        PublishGoods publishGoods = PublishGoods.builder()
                .publisherId(loginService.getOpenIDFromToken(token))
                .name(publishGoodsRequest.getGoodsName())
                .label(publishGoodsRequest.getLabel())
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

    public void getSoldGoods(String token){
        String openID = loginService.getOpenIDFromToken(token);
        List<String> goodsIds = transactionRecordMapper.selectGoodsIdByUserId(openID);
        if (CommonUtil.isNullList(goodsIds)) {
            return;
        }
        for (String goodsId : goodsIds) {

        }


    }
}
