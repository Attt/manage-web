package com.dszj.manage.enums;

import lombok.Getter;
/**
 * 菜单类型枚举
 * @author yys
 */
@Getter
public enum MenuTypeEnum {
	MODE_MENU(0, "模块菜单"),
    ONE_MENU(1, "一级菜单"),
    TWO_MENU(2, "二级菜单"),
    OPERATE(3, "操作"),
	RESOURCE(4, "资源");
	
//	   TOP_LEVEL((Integer)1, "一级菜单"),
//	    SUB_LEVEL((Integer)2, "子级菜单"),
//	    NOT_MENU((Integer)3, "不是菜单"),

    private Integer code;

    private String message;

    MenuTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    
    
}

