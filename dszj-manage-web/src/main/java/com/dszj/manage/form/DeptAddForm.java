package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DeptAddForm {
	@NotBlank(message = "部门名称不能为空")
	private String name;
	private Integer parentId;
	private Integer sort;
	private String remark;

}
