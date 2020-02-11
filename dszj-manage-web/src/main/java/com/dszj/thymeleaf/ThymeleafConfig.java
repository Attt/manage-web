package com.dszj.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dszj.thymeleaf.MyDialect;

/**
 * 
 * @author yys
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 配置自定义的CusDialect，用于整合thymeleaf模板
     */
    @Bean
    public MyDialect getMyDialect(){
        return new MyDialect();
    }
}
