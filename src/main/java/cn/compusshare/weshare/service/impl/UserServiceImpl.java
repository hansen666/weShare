package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.RequestBody.AddUserRequest;
import cn.compusshare.weshare.repository.entity.User;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.service.UserService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @Author: LZing
 * @Date: 2019/3/6
 * 用户service
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginService loginService;

    /**
     * 新增用户
     * @param token
     * @param addUserRequest
     * @return
     */
    @Override
    public ResultResponse addUser(String token, AddUserRequest addUserRequest) {
        String openID=loginService.getOpenIDFromToken(token);
        if(isUserExist(openID)){
            return ResultUtil.fail(Common.FAIL,Common.DATABASE_OPERATION_FAIL);
        }
        User user=new User();
        user.setId(openID);
        user.setAvatarUrl(addUserRequest.getAvatarUrl());
        user.setNickname(addUserRequest.getNickname());
        user.setSchoolName(addUserRequest.getSchoolName());
        int result=userMapper.insertSelective(user);
        //数据库插入失败
        if(result==0){
            return ResultUtil.fail(Common.FAIL,Common.DATABASE_OPERATION_FAIL);
        }
        return ResultUtil.success();
    }


    /**
     * 修改用户资料
     * @param user
     * @return
     */
    @Override
    public ResultResponse modify(String token, User user) {
        //String openID = loginService.getOpenIDFromToken(token);
        String openID = "testAccount1";
        user.setId(openID);
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result == 0) {
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    /**
     * 查询用户是否存在
     * @param userID
     * @return
     */
    @Override
    public boolean isUserExist(String userID) {
        int result=userMapper.isUserExist(userID);
        return result==1? true : false;
    }
}
