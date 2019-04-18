package cn.compusshare.weshare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @Author: LZing
 * @Date: 2019/4/18
 * 跨域请求配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .exposedHeaders("access-control-allow-headers","access-control-allow-methods","access-control-allow-origin","access-control-max-age","X-Frame-Options");

    }
}
