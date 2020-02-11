package com.dszj.manage.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态查询，排序枚举定义
 * @author yangyuesong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OrderBy {
	/**
	 * 排序规则 ，默认 desc
	 * @return
	 * @author yys
	 */
	SortEnum[] sort() default SortEnum.Desc;
	/**
	 * 排序字段 
	 * @return
	 * @author yys
	 */
    String[] field();
}
