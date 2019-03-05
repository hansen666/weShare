package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.IllegalUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IllegalUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IllegalUser record);

    int insertSelective(IllegalUser record);

    IllegalUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IllegalUser record);

    int updateByPrimaryKey(IllegalUser record);
}