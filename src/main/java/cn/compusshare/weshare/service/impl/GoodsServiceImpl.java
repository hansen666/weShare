package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.entity.Collection;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.TransactionRecord;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
        String msg = "待审核";
        Byte status = 0;
        if (CommonUtil.imageCensor(goodsRequest.getPicUrl(), "publish") && CommonUtil.textCensor(goodsRequest.getDescription())) {
            msg = "审核通过";
            status = 1;
        }
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
                .status(status)
                .pubTime(date)
                .build();
        try {
            publishGoodsMapper.insertSelective(publishGoods);
        } catch (Exception e) {
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success(msg);
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
        String msg = "待审核";
        Byte status = 0;
        if (CommonUtil.imageCensor(goodsRequest.getPicUrl(), "want") && CommonUtil.textCensor(goodsRequest.getDescription())) {
            msg = "审核通过";
            status = 1;
        }
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
                .status(status)
                .pubTime(date)
                .build();

        try {
            wantGoodsMapper.insertSelective(wantGoods);
        } catch (Exception e) {
            return ResultUtil.fail(-1, "数据库保存错误");
        }

        return ResultUtil.success(msg);
    }


    /**
     * 查询卖出的物品
     *
     * @param token
     * @return
     */
    @Override
    public List<Map<String, Object>> getSoldGoods(String token) {
        String openID = loginService.getOpenIDFromToken(token);
        //到交易记录表中查该用户的交易记录中的物品ID
        List<Integer> goodsIds = transactionRecordMapper.selectGoodsId(openID);
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
     * 删除我的卖出物品记录
     * @param goodsID
     * @return
     */
    @Override
    public ResultResponse removeSoldGoods(Integer[] goodsID) {
        for (Integer id : goodsID) {
            transactionRecordMapper.deleteByPrimaryKey(id);
        }
        return ResultUtil.success();
    }

    /**
     * 收藏操作
     * @param token
     * @param goodsID
     * @return
     */
    @Override
    public ResultResponse collect(String token, int goodsID) {
        String openID = loginService.getOpenIDFromToken(token);
        boolean isCollected = collectionMapper.isRecordExist(openID, goodsID) == 1 ? true : false;
        if (isCollected) {
            return ResultUtil.success();
        }
        Collection c = new Collection();
        c.setUserId(openID);
        c.setGoodsId(goodsID);
        int result = collectionMapper.insertSelective(c);
        //数据库操作失败
        if (result == 0){
            return ResultUtil.fail(Common.FAIL,Common.DATABASE_OPERATION_FAIL);
        }
        return ResultUtil.success();
    }

    /**
     * 取消收藏操作
     * @param token
     * @param goodsID
     * @return
     */
    @Override
    public ResultResponse cancelCollection(String token, Integer[] goodsID) {
        String openID = loginService.getOpenIDFromToken(token);
        for (Integer id : goodsID) {
            collectionMapper.deleteByUserIDAndGoodsID(openID, id);
        }
        return ResultUtil.success();
    }

    /**
     * 某个物品是否被收藏
     * @param token
     * @param goodsID
     * @return
     */
    @Override
    public Map<String,Boolean> isGoodsCollected(String token, int goodsID) {
        String openID = loginService.getOpenIDFromToken(token);
        boolean isCollected = collectionMapper.isRecordExist(openID, goodsID) == 1 ? true : false;
        Map<String,Boolean> result = new HashMap<>(1);
        result.put("isCollected",isCollected);
        return result;

    }


    /**
     * 我的收藏
     *
     * @param token
     * @return
     */
    @Override
    public List<Map<String, Object>> collections(String token) {
        String openID = loginService.getOpenIDFromToken(token);
        //到收藏表中查该用户的收藏记录中的物品ID
        List<Integer> goodsIds = collectionMapper.selectGoodsId(openID);
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
     * @return
     */
    @Override
    public List<Map<String, Object>> myPublish(String token) {
        String openID = loginService.getOpenIDFromToken(token);
        List<Map<String, Object>> result = publishGoodsMapper.selectMyPublish(openID);
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return result;
    }

    /**
     * 修改我的发布
     * @param token
     * @param publishGoods
     * @return
     */
    @Override
    public ResultResponse myPublishModify(String token, PublishGoods publishGoods) {
        String openID = loginService.getOpenIDFromToken(token);
        publishGoods.setPublisherId(openID);
        publishGoodsMapper.updateByPrimaryKeySelective(publishGoods);

        //文本审核未通过
        if (!CommonUtil.isEmpty(publishGoods.getDescription()) && !CommonUtil.textCensor(publishGoods.getDescription())) {
            publishGoodsMapper.updateStatus(publishGoods.getId(), (byte)0);
            return ResultUtil.success(Common.CENSOR_FAIL,Common.CENSOR_FAIL_MSG);
        }

        //图片审核
        if (!CommonUtil.isEmpty(publishGoods.getPicUrl())) {
            Boolean flag = CommonUtil.imageCensor(publishGoods.getPicUrl(), "publish");
            //审核次数受限
            if ( flag == null) {
                publishGoodsMapper.updateStatus(publishGoods.getId(), (byte)0);
                return ResultUtil.success(Common.CENSOR_TIMES_LIMIT,Common.CENSOR_TIMES_LIMIT_MSG);
            }else if (!flag) {  //审核未通过
                publishGoodsMapper.updateStatus(publishGoods.getId(), (byte)0);
                return ResultUtil.success(Common.CENSOR_FAIL,Common.CENSOR_FAIL_MSG);
            }else {  //审核通过
                publishGoodsMapper.updateStatus(publishGoods.getId(), (byte)1);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 删除发布的物品
     * @param goodsID
     * @return
     */
    @Override
    public ResultResponse removePublish(Integer[] goodsID) {
        for (Integer id : goodsID) {
            publishGoodsMapper.deleteByPrimaryKey(id);
        }
        return ResultUtil.success();
    }

    /**
     * 我的求购
     * @param token
     * @return
     */
    @Override
    public List<Map<String, Object>> myWanted(String token) {
        String openID = loginService.getOpenIDFromToken(token);
        List<Map<String, Object>> result = wantGoodsMapper.selectMyWanted(openID);
        result.forEach(map -> map.put("pubTime", CommonUtil.timeFromNow((Date) map.get("pubTime"))));
        return result;
    }

    /**
     * 修改我的求购
     * @param token
     * @param wantGoods
     * @return
     */
    @Override
    public ResultResponse myWantedModify(String token, WantGoods wantGoods) {
        String openID = loginService.getOpenIDFromToken(token);
        wantGoods.setWantBuyerId(openID);
        wantGoodsMapper.updateByPrimaryKeySelective(wantGoods);

        //文本审核未通过
        if (!CommonUtil.isEmpty(wantGoods.getDescription()) && !CommonUtil.textCensor(wantGoods.getDescription())) {
            wantGoodsMapper.updateStatus(wantGoods.getId(), (byte)0);
            return ResultUtil.success(Common.CENSOR_FAIL,Common.CENSOR_FAIL_MSG);
        }

        //求购图片审核
        if (!CommonUtil.isEmpty(wantGoods.getPicUrl())) {
            Boolean flag = CommonUtil.imageCensor(wantGoods.getPicUrl(), "want");
            //审核次数受限
            if (flag == null) {
                wantGoodsMapper.updateStatus(wantGoods.getId(), (byte)0);
                return ResultUtil.success(Common.CENSOR_TIMES_LIMIT,Common.CENSOR_TIMES_LIMIT_MSG);
            }else if (!flag) {  //图片审核未通过
                wantGoodsMapper.updateStatus(wantGoods.getId(), (byte)0);
                return ResultUtil.success(Common.CENSOR_FAIL,Common.CENSOR_FAIL_MSG);
            }else {  //审核通过
                wantGoodsMapper.updateStatus(wantGoods.getId(), (byte)1);
                return ResultUtil.success();
            }
        }
        return ResultUtil.success();
    }


    /**
     * 删除求购的物品
     * @param goodsID
     * @return
     */
    @Override
    public ResultResponse removeWanted(Integer[]  goodsID) {
        for (Integer id : goodsID) {
            wantGoodsMapper.deleteByPrimaryKey(id);
        }
        return ResultUtil.success();
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
    public ResultResponse showHomeGoods(String token, int currentPage, Byte label, String keyword, String currentTime) {
        String publisherId = loginService.getOpenIDFromToken(token);
        String key = CommonUtil.isEmpty(keyword) ? null : keyword.trim();
        try {
            List<HashMap<String, Object>> goodsList = publishGoodsMapper.selectShowGoods(publisherId,
                    5 * currentPage, label, key, userMapper.selectByPrimaryKey(publisherId).getSchoolName(), currentTime);
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
            publishGoodsMapper.browseCountIncrement(id);
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
    public ResultResponse showWishWall(String token, int currentPage, Byte label, String currentTime) {
        String wantBuyer = loginService.getOpenIDFromToken(token);
        try {
            List<HashMap<String, Object>> goodsList = wantGoodsMapper.selectWantGoods(wantBuyer, currentPage * 5,
                    label, userMapper.selectByPrimaryKey(wantBuyer).getSchoolName(), currentTime);
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
            wantGoodsMapper.browseCountIncrement(id);
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

    /**
     * 交易完成，更新状态
     * @param token
     * @Param goodsID
     * @return
     */
    @Override
    @Transactional
    public ResultResponse dealComplete(String token, int goodsID) {
        String openID = loginService.getOpenIDFromToken(token);
        TransactionRecord record = new TransactionRecord();
        record.setSellerId(openID);
        record.setGoodsId(goodsID);
        int result = 0;
        result += publishGoodsMapper.updateStatus(goodsID, (byte) 2);
        result += transactionRecordMapper.insertSelective(record);
        //数据库操作失败
        if (result != 2){
            return ResultUtil.fail(Common.FAIL,Common.DATABASE_OPERATION_FAIL);
        }
        return ResultUtil.success();
    }

}
