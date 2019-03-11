package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.PublishGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PublishGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PublishGoods record);

    int insertSelective(PublishGoods record);

    PublishGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PublishGoods record);

    int updateByPrimaryKey(PublishGoods record);
}