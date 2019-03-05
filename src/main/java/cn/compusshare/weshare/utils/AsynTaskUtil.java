package cn.compusshare.weshare.utils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: LZing
 * @Date: 2019/3/5
 * 异步任务工具类
 */
@Component
public class AsynTaskUtil {

    @Async(value = "taskExecutor")
    public void asynExecute(Runnable task) {
        task.run();

    }

}
