package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.service.HelloService;
import cn.compusshare.weshare.utils.AsynTaskUtil;
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

    @Autowired
    private AsynTaskUtil asynTaskUtil;

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

    @Override
    public void testAsynTask() {
        Thread thread=new Thread(){
            @Override
            public void run(){
                try {
                    for(int i=0;i<5;i++) {
                        Thread.sleep(3000);
                        System.out.println(i+Thread.currentThread().getName());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        asynTaskUtil.asynExecute(thread);

    }
}
