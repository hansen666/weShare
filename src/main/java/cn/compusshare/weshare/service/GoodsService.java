package cn.compusshare.weshare.service;


import cn.compusshare.weshare.repository.RequestBody.PublishGoodsRequest;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

@Service
public interface GoodsService {

    ResultResponse publishGoods(PublishGoodsRequest publishGoodsRequest);
}
