package com.dszj.manage.dto;

import lombok.Data;

/**
 * 调用微信、QQ、微博登录相关接口返回基础信息封装类
 * 
 * @author yys
 */
@Data
public class BaseResDTO {
	/**
	 * 调用接口获取信息是否成功
	 */
	private boolean isSuccess = true;
	private String errCode;
	private String errMsg;
	
}
