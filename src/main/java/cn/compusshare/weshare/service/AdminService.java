package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.RequestBody.AdminGoodsRequest;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface AdminService {

    ResultResponse login(String account, String password, HttpSession session);

    ResultResponse userQuery(String nickname, Integer type, Integer currentPage);

    ResultResponse logout(String account, String currentToken);

    ResultResponse monthlyGoodsQuantity(int year, int flag);

    ResultResponse dailyGoodsQuantity(int year, int month, int flag);

    ResultResponse monthlyUserQuantity(int year);

    ResultResponse dailyUserQuantity(int year, int month);

    ResultResponse auditFailGoods(int currentPage, int flag);

    ResultResponse changeGoodsStatus(int id, byte status, int flag);

    ResultResponse monthlyGoodsTransactionQuantity(int year);

    ResultResponse dailyGoodsTransactionQuantity(int year, int month);

    ResultResponse goodsRecord(AdminGoodsRequest request);

    ResultResponse userSold(String id);

    ResultResponse userWanted(String id);

    ResultResponse userPublish(String id);

    ResultResponse userCollections(String id);

    ResultResponse getUserFullInfo(String id);
}
