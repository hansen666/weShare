package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.service.HelloService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: LZing
 * @Date: 2019/2/28 9:59
 * 示例controller
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private HelloService helloService;



    @GetMapping("/sayHello")
    public ResultResponse sayHello(){
        logger.info("HelloController.sayHello(),入参：null");
        return ResultUtil.success("hello!");
    }

    @GetMapping("/school")
    public  ResultResponse getSchools(@RequestParam int count){
        logger.info("HelloController.getSchools(),入参：count={}",count);
        return ResultUtil.success(helloService.dataBaseTest(count));
    }

    @GetMapping("/asyn")
    public ResultResponse testAsyn(){
        helloService.testAsynTask();
        return ResultUtil.success("wait for asyn");
    }



}
