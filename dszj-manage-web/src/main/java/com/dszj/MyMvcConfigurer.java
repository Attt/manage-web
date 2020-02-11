package com.dszj;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义配置
 * @author yys
 *
 */
@Configuration
public class MyMvcConfigurer implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/").setViewName("forward:/back/login");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
	
	
}