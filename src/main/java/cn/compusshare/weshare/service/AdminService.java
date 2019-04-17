package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface AdminService {

    ResultResponse login(String account, String password, HttpSession session);

    ResultResponse userQuery(int type, int currentPage);

    ResultResponse logout(String account, String currentToken);

    ResultResponse monthlyPublishGoodsQuantity(int year);

    ResultResponse dailyPublishGoodsQuantity(int year, int month);

    ResultResponse monthlyUserQuantity(int year);

    ResultResponse dailyUserQuantity(int year, int month);

    ResultResponse auditFailGoods(int currentPage, int flag);

    ResultResponse changeGoodsStatus(int id, byte status, int flag);
}
