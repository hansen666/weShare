package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.WantGoods;
import cn.compusshare.weshare.repository.mapper.CollectionMapper;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.TransactionRecordMapper;
import cn.compusshare.weshare.repository.mapper.WantGoodsMapper;
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
import java.util.Map;


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
    public List<Map<String,Object>> getSoldGoods(String token,int currentPage){
        //TOD O
        String openID = loginService.getOpenIDFromToken(token);
       // String openID = "testAccount1";
        //到交易记录表中查该用户的交易记录中的物品ID
        List<Integer> goodsIds = transactionRecordMapper.selectGoodsId(openID,10 * currentPage);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情
        List<Map<String,Object>> soldGoodsList=new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String,Object> goods=publishGoodsMapper.selectSoldGoods(goodsId);
            if (goods != null) {
                goods.put("pubTime",CommonUtil.timeFromNow((Date)goods.get("pubTime")));
                goods.put("updateTime",CommonUtil.timeFromNow((Date)goods.get("updateTime")));
                soldGoodsList.add(goods);
            }
        }
        return soldGoodsList;
    }

    /**
     * 我的收藏
     * @param token
     * @return
     */
    @Override
    public List<Map<String,Object>> collections(String token,int currentPage) {
        //TOD O
        String openID = loginService.getOpenIDFromToken(token);
        //String openID = "testAccount1";
        //到收藏表中查该用户的收藏记录中的物品ID
        List<Integer> goodsIds = collectionMapper.selectGoodsId(openID, 10 * currentPage);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情,筛选审核中的物品
        List<Map<String,Object>> collectionGoodsList=new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String,Object> goods=publishGoodsMapper.selectCollection(goodsId);
            if (goods != null) {
                goods.put("pubTime",CommonUtil.timeFromNow((Date)goods.get("pubTime")));
                collectionGoodsList.add(goods);
            }
        }
        return collectionGoodsList;

    }

    /**
     * 我的发布
     * @param token
     * @param currentPage
     * @return
     */
    @Override
    public List<Map<String,Object>> myPublish(String token, int currentPage) {
        //T ODO
        String openID = loginService.getOpenIDFromToken(token);
       // String openID = "testAccount1";
        List<Map<String,Object>> result = publishGoodsMapper.selectMyPublish(openID, 10 * currentPage);
        result.forEach(map -> map.put("pubTime",CommonUtil.timeFromNow((Date)map.get("pubTime"))));
        return result;
    }
}
