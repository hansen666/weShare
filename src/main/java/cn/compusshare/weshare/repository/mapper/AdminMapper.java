package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    int deleteByPrimaryKey(String account);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(String account);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}