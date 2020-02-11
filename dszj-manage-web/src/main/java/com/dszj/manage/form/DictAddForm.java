package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DictAddForm {
	@NotBlank(message = "字典标识不能为空")
	private String label;
	@NotBlank(message = "字典标题不能为空")
	private String title;
	@NotBlank(message = "字典键值不能为空")
	private String value;
	private String remark;

}
