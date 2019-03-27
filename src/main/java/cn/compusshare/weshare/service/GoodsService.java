package cn.compusshare.weshare.service;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.repository.entity.PublishGoods;
import cn.compusshare.weshare.repository.entity.WantGoods;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface GoodsService {

    ResultResponse publishGoods(String token, GoodsRequest goodsRequest);

    ResultResponse wantGoods(String token, GoodsRequest goodsRequest);

    List<Map<String, Object>> getSoldGoods(String token);

    ResultResponse removeSoldGoods(Integer[] goodsID);

    ResultResponse collect(String token, int goodsID);

    ResultResponse cancelCollection(String token, Integer[] goodsID);

    Map<String, Boolean> isGoodsCollected(String token, int goodsID);

    List<Map<String, Object>> collections(String token);

    List<Map<String, Object>> myPublish(String token);

    ResultResponse showWishWall(String token, int currentPage, Byte label, String currentTime);

    ResultResponse showWishDetail(Integer id);

    ResultResponse myPublishModify(String token, PublishGoods publishGoods);

    ResultResponse removePublish(Integer[] goodsID);

    List<Map<String, Object>> myWanted(String token);

    ResultResponse myWantedModify(String token, WantGoods wantGoods);

    ResultResponse removeWanted(Integer[] goodsID);

    ResultResponse showHomeGoods(String token, int currentPage, Byte label, String keyword, String currentTime);

    ResultResponse showHomeDetail(Integer id);

    ResultResponse uploadImage(MultipartFile file, int id, String filePath);

    ResultResponse dealComplete(String token, int goodsID);
}
