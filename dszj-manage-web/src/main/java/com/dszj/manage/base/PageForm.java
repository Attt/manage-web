package com.dszj.manage.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询基类封装
 * 
 * @author yys
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class PageForm<T> extends BaseForm<T>{	
	/**
	 * 当前页号
	 */
	private Integer pageNum = 1;
	/**
	 * 每一页记录数
	 */
	private Integer pageSize = 10;
}
