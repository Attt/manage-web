package com.dszj.manage.enums;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

@Getter
public enum FileBizTypePathEnum {

	BACK_HEAD_PIC(1, "后台用户头像","/back/upload/headPic/"),
	;
	private Integer code;
	private String msg;
	private String path;
	
	FileBizTypePathEnum(Integer code, String msg,String path) {
		this.code = code;
		this.msg = msg;
		this.path= path;
	}
	
	public static String getFilePath(Integer code) {
		for (FileBizTypePathEnum backFilePathEnum : FileBizTypePathEnum.values()) {
			if (code.equals(backFilePathEnum.getCode())) {
				return backFilePathEnum.getPath().replace("yyyyMMdd", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			}
		}
		return null;
	}
	
	public static FileBizTypePathEnum getEnumByCode(Integer code) {
		for (FileBizTypePathEnum obj : FileBizTypePathEnum.values()) {
			if (code.equals(obj.getCode())) {
				return obj;
			}
		}
		return null;
	}
}
