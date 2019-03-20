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

    List<Map<String,Object>> getSoldGoods(String token, int currentPage);

    List<Map<String,Object>> collections(String token, int currentPage);

    List<Map<String,Object>> myPublish(String token, int currentPage);

    ResultResponse showWishWall(String token, int currentPage, Byte label);

    ResultResponse showWishDetail(Integer id);

    ResultResponse showHomeGoods(String token, int currentPage, Byte label, String keyword);

    ResultResponse showHomeDetail(Integer id);

    ResultResponse uploadImage(MultipartFile file, int id, String filePath);
}
