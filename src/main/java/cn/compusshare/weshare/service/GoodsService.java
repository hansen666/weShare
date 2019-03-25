package cn.compusshare.weshare.service;


import cn.compusshare.weshare.repository.RequestBody.GoodsRequest;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

@Service
public interface GoodsService {

    ResultResponse publishGoods(String token, GoodsRequest goodsRequest);

    ResultResponse wantGoods(String token, GoodsRequest goodsRequest);

    List<Map<String,Object>> getSoldGoods(String token);

    ResultResponse collect(String token, int goodsID);

    List<Map<String,Object>> collections(String token);

    List<Map<String,Object>> myPublish(String token);

    ResultResponse showWishWall(String token, int currentPage, Byte label);

    ResultResponse showWishDetail(Integer id);

    List<Map<String, Object>> myWanted(String token);

    ResultResponse showHomeGoods(String token, int currentPage, Byte label, String keyword);

    ResultResponse showHomeDetail(Integer id);

    ResultResponse uploadImage(MultipartFile file, int id, String filePath);
}
