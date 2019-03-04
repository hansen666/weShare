package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/***
 * service示例
 */
@Component
public class HelloServiceImpl implements HelloService{

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public List<School> dataBaseTest(int count){
        List<School> result=new ArrayList<>();
        for(int i=0;i<count;i++) {
            School school=schoolMapper.selectByPrimaryKey(i);
            if(school!=null)
                result.add(school);
        }
        return result;
    }
}
