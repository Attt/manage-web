package com.dszj.manage.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 微信accessToken信息
 * 
 * @author yys
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class WeChatAccessTokenDTO extends BaseResDTO{
	
	private String accessToken;
	private String openId;
	private String refreshToken;
	private String expiresIn;
	private String scope;
	
}
