package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.School;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(School record);

    int insertSelective(School record);

    School selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(School record);

    int updateByPrimaryKey(School record);
}