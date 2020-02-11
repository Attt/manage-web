package com.dszj.manage.auth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * 自定义mvc配置
 * @author yys
 */
@Configuration
public class GlobalWebMvcConfig implements WebMvcConfigurer  {
	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	@Autowired
	private LoginAccountMethodArgumentResolver loginAccountMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**");
//        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginAccountMethodArgumentResolver);
//        argumentResolvers.add(currentAccountInfoMethodArgumentResolver());
    }


//    @Bean
//    public LoginAccountMethodArgumentResolver currentAccountInfoMethodArgumentResolver() {
//        return new LoginAccountMethodArgumentResolver();
//    }
//
//    @Bean
//    public AuthenticationInterceptor authenticationInterceptor() {
//        return new AuthenticationInterceptor();
//    }

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**")
		.allowedMethods("GET","POST")
		.allowedOrigins("*")
//		.allowedOrigins("http://www.dszj1.cn","https://www.dszj1.cn")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
    
    
}
