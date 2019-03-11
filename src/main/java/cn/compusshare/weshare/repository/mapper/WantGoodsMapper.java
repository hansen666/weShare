package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.WantGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WantGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WantGoods record);

    int insertSelective(WantGoods record);

    WantGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WantGoods record);

    int updateByPrimaryKey(WantGoods record);
}