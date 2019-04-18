package cn.compusshare.weshare.service.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: LZing
 * @Date: 2019/3/7
 * redis缓存单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void redisTest() {


    }

    //List、Set等集合测试
    @Test
    public void redisCollectionTest(){
        cacheService.set("oRpmu4svAH_Voq3peuz8rUGzYA1s"+"cus","tokenvalue");
        String token =(String) cacheService.get("oRpmu4svAH_Voq3peuz8rUGzYA1scus");
        System.out.println(token);

    }

}