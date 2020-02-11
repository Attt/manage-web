package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RoleAddForm {
	@NotBlank(message = "角色标识不能为空")
	private String name;
	@NotBlank(message = "角色名称不能为空")
	private String title;
	private String remark;

}
