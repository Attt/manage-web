package com.dszj.manage.auth;

import java.util.List;

import com.dszj.manage.entity.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营用户缓存对象
 * @author yys
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfo extends BaseAccountInfo {
	
	private Integer id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 登录用户名
     */
    private String username;
    
    /**
     * 角色组
     */
    private List<Role> roles;
    
}
