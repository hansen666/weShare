package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.WantGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface WantGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WantGoods record);

    int insertSelective(WantGoods record);

    WantGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WantGoods record);

    int updateByPrimaryKey(WantGoods record);

    List<HashMap<String, Object>> selectWantGoods(@Param("wantBuyerId") String wantBuyerId,@Param("pageIndex") int pageIndex,
                                                  @Param("school") String school);

}