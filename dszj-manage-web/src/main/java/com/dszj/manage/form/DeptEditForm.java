package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeptEditForm {
	@NotNull(message = "部门ID不能为空")
	private Integer id;
	@NotBlank(message = "部门名称不能为空")
	private String name;
	private Integer parentId;
	private Integer sort;
	private String remark;

}
