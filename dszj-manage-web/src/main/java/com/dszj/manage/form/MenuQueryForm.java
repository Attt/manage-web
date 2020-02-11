package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.OptEnum;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.Menu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuQueryForm extends PageForm<Menu> {
	@DynamicWhere
    private String label;
	@DynamicWhere(opt=OptEnum.Like)
	private String name;
	@DynamicWhere
	private String url;
	@DynamicWhere
	private Integer parentId;
	
}
