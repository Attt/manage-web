package com.dszj.manage.enums;

import lombok.Getter;
/**
 * 全局常量枚举
 * @author yys
 */
@Getter
public enum GloableParamEnum {
	DEFAULT_HEAD_PIC("/back/images/headPic.png","后台用户默认头像"),
  ;
	
    private String value;
    private String msg;

    GloableParamEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }
}

