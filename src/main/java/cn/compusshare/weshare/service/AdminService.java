package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    ResultResponse userQuery(int type);
}
