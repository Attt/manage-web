package com.dszj.manage.enums;

import lombok.Getter;
/**
 * 文件存储目录枚举
 * @author yys
 */
@Getter
public enum FilePathEnum {
	HEAD_PIC("uploads/back/headPic/","头像上传目录"),
  ;
	
    private String value;
    private String msg;

    FilePathEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }
    
    public String getPath(String fileName){
    	return "/"+this.getValue()+fileName;
    }
}

