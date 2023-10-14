package com.kgc.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiang on 8/9/23 4:51 PM
 */
@Configuration
public class PageConfiguration {

    //使用mybatis-plus分页都需要添加这个配置类
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
