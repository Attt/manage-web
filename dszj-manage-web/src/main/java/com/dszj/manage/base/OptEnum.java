package com.dszj.manage.base;

import lombok.Getter;

/**
 * 动态查询操作枚举
 * @author yys
 */
@Getter
public enum OptEnum {	
	/**
	 * = ?
	 */
	Equals,
	/**
	 * != ?
	 */
	NotEquals,
	
	/**
	 * like '%?%'
	 */
	Like,
	/**
	 * like '?%'
	 */
	LeftLike,
	/**
	 * like '%?'
	 */
	RightLike,
	/**
	 * > ?
	 */
	GreaterThan,
	/**
	 * >= ?
	 */
	GreaterThanEquals,
	/**
	 * < ?
	 */
	LessThan,
	/**
	 *  <= ?
	 */
	LessThanEquals,
	/**
	 * is null
	 */
//	IsNull,
	/**
	 * is not null
	 */
//	IsNotNull,
	/**
	 * in(?)
	 */
	In,
	;
}
