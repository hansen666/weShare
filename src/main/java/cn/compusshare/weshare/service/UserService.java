package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.RequestBody.AddUserRequest;
import cn.compusshare.weshare.repository.entity.User;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

/**
 * @Author: LZing
 * @Date: 2019/3/6
 * 用户管理Service
 */
@Service
public interface UserService {


    ResultResponse addUser(String token, AddUserRequest addUserRequest);

    ResultResponse modify(String token, User user);

    boolean isUserExist(String userID);
}
