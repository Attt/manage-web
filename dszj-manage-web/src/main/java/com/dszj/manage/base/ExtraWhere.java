package com.dszj.manage.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 附加查询条件注解定义
 * (只适用于枚举类型、状态常量)
 * 
 * @author yangyuesong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("rawtypes")
public @interface ExtraWhere {
	
	/**
	 * 查询条件字段名
	 * @return
	 * @author yys
	 */
	String[] field();
	/**
	 * 查询条件字段值
	 * @return
	 * @author yys
	 */

	String[] value();
	/**
	 * 查询条件连接操作符
	 * @return
	 * @author yys
	 */

	OptEnum[] opt() default OptEnum.Equals;
	
	/**
	 * 字符枚举类名或常量类名
	 * @return
	 * @author yys
	 */
	Class[] fieldEnum();
}
