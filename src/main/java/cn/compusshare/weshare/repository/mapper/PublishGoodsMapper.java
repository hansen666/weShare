package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.PublishGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PublishGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PublishGoods record);

    int insertSelective(PublishGoods record);

    PublishGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PublishGoods record);

    int updateByPrimaryKey(PublishGoods record);

    List<HashMap<String, Object>> selectShowGoods(String publisherId, int pageIndex, Byte label, String keyword);
}