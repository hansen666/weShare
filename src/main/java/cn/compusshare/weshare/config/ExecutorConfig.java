package cn.compusshare.weshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * @Author: LZing
 * @Date: 2019/3/5
 * 线程池配置
 */
@Configuration
public class ExecutorConfig {

    //主要参数配置
    private int corePoolSize=5;
    private int maxPoolSize=10;
    private int queueCapacity=10;

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);  //设置核心线程数
        executor.setMaxPoolSize(maxPoolSize);   //设置最大线程数
        executor.setQueueCapacity(queueCapacity);  //设置队列大小
        executor.setThreadNamePrefix("自定义线程池-");
        executor.setBeanName("taskExecutor");
        executor.initialize();
        return executor;
    }
}
