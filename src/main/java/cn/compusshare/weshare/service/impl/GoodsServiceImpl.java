package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.WantGoods;
import cn.compusshare.weshare.repository.mapper.CollectionMapper;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.TransactionRecordMapper;
import cn.compusshare.weshare.repository.mapper.WantGoodsMapper;
import cn.compusshare.weshare.repository.responsebody.GoodsInfo;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


@Component
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private PublishGoodsMapper publishGoodsMapper;

    @Autowired
    private WantGoodsMapper wantGoodsMapper;

    @Override
    public ResultResponse publishGoods(String token, GoodsRequest goodsRequest) {
        Date date = new Date();
        PublishGoods publishGoods = PublishGoods.builder()
                .publisherId(loginService.getOpenIDFromToken(token))
                .name(goodsRequest.getGoodsName())
                .label(goodsRequest.getLabel())
                .description(goodsRequest.getDescription())
                .price(goodsRequest.getPrice())
                .picUrl(goodsRequest.getPicUrl())
                .phone(goodsRequest.getPhone())
                .longitude(goodsRequest.getLongitude())
                .latitude(goodsRequest.getLatitude())
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

    @Override
    public ResultResponse wantGoods(String token, GoodsRequest goodsRequest){
        Date date = new Date();
        WantGoods wantGoods = WantGoods.builder()
                .wantBuyerId(loginService.getOpenIDFromToken(token))
                .name(goodsRequest.getGoodsName())
                .label(goodsRequest.getLabel())
                .description(goodsRequest.getDescription())
                .price(goodsRequest.getPrice())
                .picUrl(goodsRequest.getPicUrl())
                .phone(goodsRequest.getPhone())
                .longitude(goodsRequest.getLongitude())
                .latitude(goodsRequest.getLatitude())
                .browseCount(0)
                .status((byte)0)
                .pubTime(date)
                .build();

        try {
            wantGoodsMapper.insertSelective(wantGoods);
        } catch (Exception e){
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success();
    }


    /**
     * 查询卖出的物品
     * @param token
     * @return
     */
    @Override
    public List<GoodsInfo> getSoldGoods(String token){
        //  String openID = loginService.getOpenIDFromToken(token);
        String openID = "testAccount1";
        List<Integer> goodsIds = transactionRecordMapper.selectGoodsId(openID);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        List<GoodsInfo> goodsInfoList=new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            PublishGoods goods=publishGoodsMapper.selectByPrimaryKey(goodsId);
            if (goods != null) {
                GoodsInfo goodsInfo= GoodsInfo.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .label(goods.getLabel())
                        .description(goods.getDescription())
                        .price(goods.getPrice())
                        .phone(goods.getPhone())
                        //.time(CommonUtil.getDate(goods.getUpdateTime()))
                        .build();
                if (!CommonUtil.isEmpty(goods.getPicUrl())) {
                    goodsInfo.setPicUrl(goods.getPicUrl().split(","));
                }
                goodsInfoList.add(goodsInfo);
            }
        }
        return goodsInfoList;
    }


    /**
     * 我的收藏
     * @param token
     * @return
     */
    public List<GoodsInfo> collections(String token) {
        //String openID = loginService.getOpenIDFromToken(token);
        String openID = "testAccount1";
        List<Integer> goodsIds = collectionMapper.selectGoodsId(openID);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        List<GoodsInfo> goodsInfoList=new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            PublishGoods goods=publishGoodsMapper.selectByPrimaryKey(goodsId);
            if (goods != null) {
                GoodsInfo goodsInfo= GoodsInfo.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .label(goods.getLabel())
                        .description(goods.getDescription())
                        .price(goods.getPrice())
                        .phone(goods.getPhone())
                        .status(goods.getStatus())
 //                       .time(CommonUtil.getDate(goods.getPubTime()))
                        .build();
                if (!CommonUtil.isEmpty(goods.getPicUrl())) {
                    goodsInfo.setPicUrl(goods.getPicUrl().split(","));
                }
                goodsInfoList.add(goodsInfo);
            }
        }
        return goodsInfoList;

    }
}
