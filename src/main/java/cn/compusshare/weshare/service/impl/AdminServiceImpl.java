package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.service.AdminService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AdminServiceImpl implements AdminService {

    private final static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultResponse userQuery(int type){
        try {
            List<Map<String, Object>> userList = userMapper.selectUserByType(type);
            return ResultUtil.success(userList);
        }catch (Exception e){
            logger.info("用户数据库查询错误");
            return  ResultUtil.fail(-1, e.getMessage());
        }
    }

}
