package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.WantGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface WantGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WantGoods record);

    int insertSelective(WantGoods record);

    WantGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WantGoods record);

    int updateByPrimaryKey(WantGoods record);

    List<HashMap<String, Object>> selectWantGoods(@Param("wantBuyerId") String wantBuyerId, @Param("currentPage") int currentPage,
                                                  @Param("label") Byte label, @Param("school") String school, @Param("currentTime") String currentTime);

    Map<String, Object> showGoodsDetail(Integer id);

    int browseCountIncrement(Integer id);

    List<Map<String, Object>> selectMyWanted(@Param("userID") String userID);

    int updateStatus(@Param("id") Integer id, @Param("status") byte status);

}