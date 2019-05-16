package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.PublishGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface PublishGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PublishGoods record);

    int insertSelective(PublishGoods record);

    PublishGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PublishGoods record);

    int updateByPrimaryKey(PublishGoods record);

    Map<String, Object> selectCollection(Integer id);

    List<HashMap<String, Object>> selectShowGoods(@Param("publisherId") String publisherId, @Param("currentPage")
            int currentPage, @Param("label") Byte label, @Param("keyword") String keyword,
                                                  @Param("school") String school, @Param("currentTime") String currentTime);

    Map<String, Object> selectSoldGoods(Integer id);

    List<Map<String, Object>> selectMyPublish(@Param("userID") String userID);

    Map<String, Object> showGoodsDetail(Integer id);

    int browseCountIncrement(Integer id);

    int updateStatus(@Param("id") Integer id, @Param("status") byte status);

    int updateImage(@Param("id") Integer id, @Param("picUrl") String picUrl);

    List<Map<String, Object>> monthlyQuantity(Integer year);

    List<Map<String, Object>> dailyQuantity(@Param("year") Integer year, @Param("month") Integer month);

    List<Map<String, Object>> auditFailGoods(Integer Index);

    List<Map<String, Object>> selectAdminGoods(@Param("goodsName") String goodsName, @Param("nickname") String nickname, @Param("label") Byte label,
                                               @Param("status") Byte status, @Param("startDate") String startDate, @Param("endDate") String endDate,
                                               @Param("schoolName") String schoolName, @Param("currentIndex") Integer currentIndex);
    int selectAdminCount(@Param("goodsName") String goodsName, @Param("nickname") String nickname, @Param("label") Byte label,
                         @Param("status") Byte status, @Param("startDate") String startDate, @Param("endDate") String endDate,
                         @Param("schoolName") String schoolName);
}