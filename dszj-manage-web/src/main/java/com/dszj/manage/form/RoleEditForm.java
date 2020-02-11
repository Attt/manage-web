package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleEditForm {
	@NotNull(message = "角色Id不能为空")
	private Integer id;
	@NotBlank(message = "角色标识不能为空")
	private String name;
	@NotBlank(message = "角色名称不能为空")
	private String title;
	private String remark;

}
