package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MeunAddForm {
	
	private Integer parentId;
	@NotBlank(message = "菜单类型不能为空")
	private String name;
	@NotNull(message = "菜单名称不能为空")
	private Integer type;
	private String url;
	private String label;
	private String icon;
	private Integer orderNum;
	private Integer status;
	private String remark;
	
}
