package cn.compusshare.weshare.service;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.responsebody.GoodsInfo;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface GoodsService {

    ResultResponse publishGoods(String token, GoodsRequest goodsRequest);

    ResultResponse wantGoods(String token, GoodsRequest goodsRequest);

    List<GoodsInfo> getSoldGoods(String token);
}
