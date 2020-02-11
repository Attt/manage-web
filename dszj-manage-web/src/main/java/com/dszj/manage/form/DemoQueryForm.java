package com.dszj.manage.form;

import java.math.BigDecimal;
import java.util.List;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.OptEnum;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.Demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DemoQueryForm extends PageForm<Demo> {
	
	@DynamicWhere(field="id",opt=OptEnum.In)
	private List<Integer> ids;
//	
	@DynamicWhere(field="title")
    private String title;
	
	@DynamicWhere(field="longNum")
	private Long longNum;
	
	@DynamicWhere(field="floatNum")
	private Float floatNum;
	
	@DynamicWhere(field="doubleNum")
	private Double doubleNum;
	
	@DynamicWhere(field="bigDecimalNum")
	private BigDecimal bigDecimalNum;	
	
//	@DynamicWhere(field="createTime")	
//	private java.sql.Date createTime;	
//	
//	@DynamicWhere(field="updateTime")
//	private Timestamp updateTime;
	
}
