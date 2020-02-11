package com.dszj.manage.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 前台用户信息缓存对象
 * @author yys
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MemberInfo extends BaseAccountInfo{
	
	private Integer id;
    /**
     * 昵称
     */
    private String nickname;
    
    
}
