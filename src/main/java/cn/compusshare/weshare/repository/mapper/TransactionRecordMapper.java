package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);

    List<String> selectGoodsIdByUserId(String userID);
}