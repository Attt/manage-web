package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.OptEnum;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.Dept;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeptQueryForm extends PageForm<Dept> {
	@DynamicWhere
	private Integer id;
	@DynamicWhere(opt=OptEnum.Like)
    private String name;
	
}
