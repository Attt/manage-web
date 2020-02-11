package com.dszj.manage.exception;

import lombok.Getter;

/**
 * 
 * @author yys
 */
@Getter
public enum BizExceptionEnum {

	// 常见异常定义
	SYS_ERROR("CUST0001", "系统开小差了，请稍后再试"),
	PARAM_ERROR("CUST0002", "参数错误"),
	;

	private String code;

	private String msg;

	BizExceptionEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String getMsgByCode(String code) {
		for (BizExceptionEnum resultEnum : BizExceptionEnum.values()) {
			if (code.equals(resultEnum.getCode())) {
				return resultEnum.getMsg();
			}
		}
		return null;
	}

}
