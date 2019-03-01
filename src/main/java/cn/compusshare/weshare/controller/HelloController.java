package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.service.HelloService;
import cn.compusshare.weshare.utils.PlatformResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.SaslClient;
import java.lang.management.PlatformLoggingMXBean;
import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/2/28 9:59
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private HelloService helloService;

    @GetMapping("/sayHello")
    public PlatformResult sayHello(){
        return PlatformResult.success(Common.SUCCESS,"hello!");
    }

    @GetMapping("school")
    public PlatformResult<List<School>> getSchools(@RequestParam int count){
        logger.info("HelloController.getSchools(),入参：count={}",count);
        PlatformResult<List<School>> platformResult=PlatformResult.success(Common.SUCCESS,helloService.dataBaseTest(count));
        return platformResult;
    }

}
