package com.dszj.manage.exception;

import lombok.Getter;

/**
 * 用户服务异常类定义
 * @author yys
 */
@SuppressWarnings("serial")
@Getter
public class BizException extends RuntimeException {

	private String code;
	private String msg;
	private String errorDesc;
	
    public BizException(BizExceptionEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }
	
    public BizException(BizExceptionEnum resultEnum, String msg) {
        super(msg);
        this.code = resultEnum.getCode();
        this.msg = msg;
    }
    
    public BizException(BizExceptionEnum resultEnum, String msg,String errorDesc) {
    	super(msg);
    	this.code = resultEnum.getCode();
    	this.msg = msg;
    	this.errorDesc = errorDesc;
    }

}
