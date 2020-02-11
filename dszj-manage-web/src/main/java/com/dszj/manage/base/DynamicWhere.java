package com.dszj.manage.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态查询条件注解定义
 * @author yangyuesong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DynamicWhere {
	/**
	 * 查询条件字段名，默认与form中注解字段名一致
	 * @return
	 * @author yys
	 */
    String field() default "";
    /**
     * 默认使用 = 操作符
     * @return
     * @author yys
     */
    OptEnum opt() default OptEnum.Equals;
}
