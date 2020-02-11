package com.dszj.manage.form;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class DemoAddForm{
	
    private String title;
	
	private Long longNum;
	
	private Float floatNum;
	
	private Double doubleNum;
	
	private BigDecimal bigDecimalNum;	
	
//	private Date createTime;	
//	
//	private Date updateTime;
	
}
