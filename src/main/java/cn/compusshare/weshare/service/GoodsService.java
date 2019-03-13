package cn.compusshare.weshare.service;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

@Service
public interface GoodsService {

    ResultResponse publishGoods(String token, GoodsRequest goodsRequest);

    ResultResponse wantGoods(String token, GoodsRequest goodsRequest);
}
