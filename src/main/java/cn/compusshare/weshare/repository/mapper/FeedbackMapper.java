package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FeedbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    Feedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKey(Feedback record);

    List<Map<String, Object>> selectFeedback(Integer currentIndex);

    int selectCount();

}