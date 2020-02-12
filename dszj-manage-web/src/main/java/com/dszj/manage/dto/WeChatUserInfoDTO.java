package com.dszj.manage.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 微信用户信息
 * 
 * @author yys
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class WeChatUserInfoDTO extends BaseResDTO{
	
	private String nickname;
	/**
	 * 1男 2女
	 */
	private String sex;
	private String province;
	private String city;
	private String country;
	private String privilege;
	private String headImgUrl;
	private String openId;
	private String unionId;
	
}
