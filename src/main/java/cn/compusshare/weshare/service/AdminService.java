package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface AdminService {
    ResultResponse login(String account, String password, HttpSession session);
}
