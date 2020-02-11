package com.dszj.manage.base;

import lombok.Getter;

/**
 * 服务接口返回状态信息枚举定义
 * @author yys
 */
@Getter
public enum ResultEnum {

	SUCCESS("0", "成功"),
	SYS_ERROR("SYS0001", "系统开小差了，请稍后再试"),
	PERMISSION_DENIED("SYS0002", "没有权限"),
	NOT_LOGIN("SYS0003", "请先登录"),
	BIZ_ERROR("SYS0004", "系统开小差了，请稍后再试"),
	;

	private String code;

	private String msg;

	ResultEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	/**
	 * 通过枚举code获取枚举msg
	 * @param code
	 * @return
	 */
	public static String getMsgByCode(String code) {
		for (ResultEnum resultEnum : ResultEnum.values()) {
			if (code.equals(resultEnum.getCode())) {
				return resultEnum.getMsg();
			}
		}
		return null;
	}
	/**
	 * 通过枚举code获取枚举对象
	 * @param code
	 * @return
	 */
	public static ResultEnum getResultEnumByCode(String code) {
		for (ResultEnum resultEnum : ResultEnum.values()) {
			if (code.equals(resultEnum.getCode())) {
				return resultEnum;
			}
		}
		return null;
	}

}
