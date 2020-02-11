package com.dszj.manage.auth;

import lombok.Getter;

/**
 * session缓存key枚举
 * @author yys
 */
@Getter
public enum SessionKeyEnum {
	ACCOUNT("ACCOUNT", "登录账户信息"),
	AUTH("AUTH", "权限信息"),
	LOGIN_CODE("LOGIN_CODE", "登录验证码"),
	;

	private String code;
	/**
	 * 客服
	 */
	private String msg;

	SessionKeyEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}


}