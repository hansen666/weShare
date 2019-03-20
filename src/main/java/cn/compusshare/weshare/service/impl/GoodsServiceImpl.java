package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.WantGoods;
import cn.compusshare.weshare.repository.mapper.*;
import cn.compusshare.weshare.repository.mapper.CollectionMapper;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.TransactionRecordMapper;
import cn.compusshare.weshare.repository.mapper.WantGoodsMapper;
import cn.compusshare.weshare.repository.responsebody.ImageResponse;
import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Component
public class GoodsServiceImpl implements GoodsService {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

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

    @Autowired
    private UserMapper userMapper;

    /**
     * 首页物品发布
     *
     * @param token
     * @param goodsRequest
     * @return
     */
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
                .status((byte) 0)
                .pubTime(date)
                .build();
        try {
            publishGoodsMapper.insertSelective(publishGoods);
        } catch (Exception e) {
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success();
    }

    /**
     * 心愿墙物品发布
     *
     * @param token
     * @param goodsRequest
     * @return
     */
    @Override
    public ResultResponse wantGoods(String token, GoodsRequest goodsRequest) {
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
                .status((byte) 0)
                .pubTime(date)
                .build();

        try {
            wantGoodsMapper.insertSelective(wantGoods);
        } catch (Exception e) {
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success();
    }


    /**
     * 查询卖出的物品
     *
     * @param token
     * @return
     */
    @Override
    public List<Map<String, Object>> getSoldGoods(String token, int currentPage) {
        //TOD O
        String openID = loginService.getOpenIDFromToken(token);
        // String openID = "testAccount1";
        //到交易记录表中查该用户的交易记录中的物品ID
        List<Integer> goodsIds = transactionRecordMapper.selectGoodsId(openID, 10 * currentPage);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情
        List<Map<String, Object>> soldGoodsList = new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String, Object> goods = publishGoodsMapper.selectSoldGoods(goodsId);
            if (goods != null) {
                goods.put("pubTime", CommonUtil.timeFromNow((Date) goods.get("pubTime")));
                goods.put("updateTime", CommonUtil.timeFromNow((Date) goods.get("updateTime")));
                soldGoodsList.add(goods);
            }
        }
        return soldGoodsList;
    }

    /**
     * 我的收藏
     *
     * @param token
     * @return
     */
    @Override
    public List<Map<String, Object>> collections(String token, int currentPage) {
        //TOD O
        String openID = loginService.getOpenIDFromToken(token);
        //String openID = "testAccount1";
        //到收藏表中查该用户的收藏记录中的物品ID
        List<Integer> goodsIds = collectionMapper.selectGoodsId(openID, 10 * currentPage);
        if (CommonUtil.isNullList(goodsIds)) {
            return null;
        }
        //根据物品ID查物品详情,筛选审核中的物品
        List<Map<String, Object>> collectionGoodsList = new ArrayList<>();
        for (Integer goodsId : goodsIds) {
            Map<String, Object> goods = publishGoodsMapper.selectCollection(goodsId);
            if (goods != null) {
                goods.put("pubTime", CommonUtil.timeFromNow((Date) goods.get("pubTime")));
                collectionGoodsList.add(goods);
            }
        }
        return collectionGoodsList;

    }

    /**
     * 我的发布
     *
     * @param token
     * @param currentPage
     * @return
     */
    @Override
    public List<Map<String, Object>> myPublish(String token, int currentPage) {
        //T ODO
        String openID = loginService.getOpenIDFromToken(token);
        // String openID = "testAccount1";
        List<Map<String, Object>> result = publishGoodsMapper.selectMyPublish(openID, 10 * currentPage);
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return result;
    }
    /**
     * 首页物品展示
     *
     * @param token
     * @param currentPage
     * @param label
     * @param keyword
     * @return
     */
    @Override
    public ResultResponse showHomeGoods(String token, int currentPage, Byte label, String keyword) {
        String publisherId = "tcz"; // loginService.getOpenIDFromToken(token);
        String key = CommonUtil.isEmpty(keyword) ? null : keyword.trim();
        try {
            List<HashMap<String, Object>> goodsList = publishGoodsMapper.selectShowGoods(publisherId,
                    6 * currentPage, label, key, userMapper.selectByPrimaryKey(publisherId).getSchoolName());
            goodsList.forEach(t -> t.put("pubTime", CommonUtil.timeFromNow((Date) t.get("pubTime"))));
            return ResultUtil.success(goodsList);
        } catch (Exception e) {
            logger.info("首页商品数据库查询失败" + e.getMessage());
            return ResultUtil.fail(-1, "首页商品数据库查询失败" + e.getMessage());
        }
    }

    /**
     * 首页物品详情页
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse showHomeDetail(Integer id) {
        try {
            Map<String, Object> result = publishGoodsMapper.showGoodsDetail(id);
            result.put("pubTime", CommonUtil.timeFromNow((Date) result.get("pubTime")));
            return ResultUtil.success(result);
        } catch (Exception e) {
            logger.info("id={}的首页物品查询错误", id);
            return ResultUtil.fail(-1, "id=" + id + "的首页物品查询错误");
        }
    }

    /**
     * 心愿墙物品展示
     *
     * @param token
     * @param currentPage
     * @return
     */
    @Override
    public ResultResponse showWishWall(String token, int currentPage, Byte label) {
        String wantBuyer = "tcz"; //loginService.getOpenIDFromToken(token);
        try {
            List<HashMap<String, Object>> goodsList = wantGoodsMapper.selectWantGoods(wantBuyer, currentPage * 6,
                    label, userMapper.selectByPrimaryKey(wantBuyer).getSchoolName());
            goodsList.forEach(t -> t.put("pubTime", CommonUtil.timeFromNow((Date) t.get("pubTime"))));
            return ResultUtil.success(goodsList);

        } catch (Exception e) {
            logger.info("心愿墙物品数据库查询失败" + e.getMessage());
            return ResultUtil.fail(-1, "心愿墙物品数据库查询失败" + e.getMessage());
        }

    }

    /**
     * 心愿墙的物品详情
     *
     * @param id
     * @return
     */
    @Override
    public ResultResponse showWishDetail(Integer id) {
        try {
            Map<String, Object> result = wantGoodsMapper.showGoodsDetail(id);
            result.put("pubTime", CommonUtil.timeFromNow((Date) result.get("pubTime")));
            return ResultUtil.success(result);
        } catch (Exception e) {
            logger.info("id={}的心愿墙物品查询错误", id);
            return ResultUtil.fail(-1, "id=" + id + "的心愿墙物品查询错误");
        }

    }

    /**
     * 图片上传
     *
     * @param file
     * @param id
     * @param filePath
     * @return
     */
    @Override
    public ResultResponse uploadImage(MultipartFile file, int id, String filePath) {
        Random rand = new Random();
        String originName = file.getOriginalFilename();
        String typeName = originName.substring(originName.lastIndexOf("."));
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);
        int randNum = rand.nextInt(899) + 100;
        String fileName = nowTimeStamp + randNum + typeName;
        File newFile = new File(filePath + fileName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.info("图片{}上传失败", originName);
            return ResultUtil.fail(-1, "图片" + originName + "上传失败");
        }
        ImageResponse imageResponse = ImageResponse.builder()
                .fileName(fileName)
                .id(id)
                .build();

        return ResultUtil.success(imageResponse);
    }

}
