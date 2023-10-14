package com.kgc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by jiang on 8/14/23 5:18 PM
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 将浏览器请求映射到本地请求 浏览器访问localhost:8181:/community/upload/face/ 的时候 到本地找对应文件
     * 本地文件位置写在配置文件中
     */

    @Value("${upload.face}")
    String face;
    @Value("${upload.excel}")
    String excel;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/community/upload/face/**").addResourceLocations("file:"+face);
        registry.addResourceHandler("/community/upload/excel/**").addResourceLocations("file:"+excel);
    }

}
