package com.dszj.manage.entity;

import lombok.Getter;
/**
 * 角色枚举
 * @author yys
 */
@Getter
public enum RoleEnum {
	ADMIN("ADMIN", "管理组"),
    BD("BD", "业务组"),
    OPERATOR("OPERATOR", "运维组"),
    DEVEPLOER("DEVEPLOER", "开发组");

    private String code;

    private String name;

    RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    
    
}

