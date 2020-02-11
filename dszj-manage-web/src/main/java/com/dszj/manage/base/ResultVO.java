package com.dszj.manage.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 服务调用统一返回封装类
 * @author yys 
 * @param <T>
 */
@Data
public class ResultVO<T> {
	/**
	 * 状态码 （默认值:0）
	 * 
	 */
    private String code = ResultEnum.SUCCESS.getCode();
	/**
	 * 状态提示信息 （默认值:成功）
	 */
    private String msg = ResultEnum.SUCCESS.getMsg();
    /**
     * 错误描述信息
     */
    private String errDesc;
    /**
     * 错误跟踪id
     */
    private String traceId;
    /**
     * 业务数据
     */
    private T data;
    
    
    //分页参数
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    
    
	public ResultVO() {
		super();
	}
	public ResultVO(T data) {
		super();
		this.data=data;
	}
    
	public ResultVO(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public ResultVO(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public ResultVO(ResultEnum resultEnum) {
		super();
		this.code = resultEnum.getCode();
		this.msg = resultEnum.getMsg();
	}
	public ResultVO(ResultEnum resultEnum,String errorDesc) {
		super();
		this.code = resultEnum.getCode();
		this.msg = resultEnum.getMsg();
		this.errDesc = errorDesc;
	}
	
	/**
	 * 判断是否调用失败  true 是  false 否
	 * @return
	 * @author yys
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	public boolean isFail() {
		if (ResultEnum.SUCCESS.getCode().equals(this.code)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否调用成功  true 是  false 否
	 * @return
	 * @author yys
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	public boolean isSuccess() {
		if (ResultEnum.SUCCESS.getCode().equals(this.code)) {
			return true;
		}
		return false;
	}
    
}
