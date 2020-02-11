package com.dszj.manage.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义当前登录账户注解
 * @author yys
 */
@Target(ElementType.PARAMETER)    
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAccount {
} 