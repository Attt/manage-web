package com.dszj.manage.enums;

import lombok.Getter;
/**
 * 是否是子节点（计算规则不含资源节点）
 * @author yys
 */
@Getter
public enum MenuIsLeafEnum {
	YES(0, "是"),
    NO(1, "否");
	
    private Integer code;
    private String msg;

    MenuIsLeafEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    
    
}

