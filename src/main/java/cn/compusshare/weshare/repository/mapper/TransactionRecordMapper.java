package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);
}