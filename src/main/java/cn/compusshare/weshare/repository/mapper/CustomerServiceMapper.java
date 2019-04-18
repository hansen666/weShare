package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.CustomerService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerServiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerService record);

    int insertSelective(CustomerService record);

    CustomerService selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerService record);

    int updateByPrimaryKey(CustomerService record);

    List<String> selectQuestions();

    String selectAnswerByKey(Integer id);
}