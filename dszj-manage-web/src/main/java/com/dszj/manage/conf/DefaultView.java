package com.dszj.manage.conf;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置登录页为项目首页
 * @author yys
 *
 */
//@Configuration
//public class DefaultView implements WebMvcConfigurer{
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/").setViewName("forward:/back/login");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
//}