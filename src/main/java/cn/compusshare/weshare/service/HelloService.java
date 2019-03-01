package cn.compusshare.weshare.service;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.utils.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelloService {

    @Autowired
    private SchoolMapper schoolMapper;

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
