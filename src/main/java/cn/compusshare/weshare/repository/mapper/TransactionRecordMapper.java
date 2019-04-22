package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);

    List<Integer> selectGoodsId(@Param("userID") String userID);

    int deleteByUserIDAndGoodsID(@Param("userID") String userID, @Param("goodsID") Integer goodsID);

    int deleteByGoodsID(@Param("goodsID") Integer goodsID);

    List<Map<String, Object>> monthlyQuantity(Integer year);

    List<Map<String, Object>> dailyQuantity(@Param("year") Integer year, @Param("month") Integer month);
}