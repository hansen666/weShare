package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int isUserExist(String userID);

    byte selectIdentifiedType(String userID);
}