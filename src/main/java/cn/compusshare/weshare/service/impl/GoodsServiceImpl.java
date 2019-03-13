package cn.compusshare.weshare.service.impl;


import cn.compusshare.weshare.service.GoodsService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.stereotype.Component;

@Component
public class GoodsServiceImpl implements GoodsService {


    @Override
    public ResultResponse publishGoods(){


        return ResultUtil.success();
    }
}
