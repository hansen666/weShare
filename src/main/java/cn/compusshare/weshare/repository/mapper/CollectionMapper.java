package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);
}