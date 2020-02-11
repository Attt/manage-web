package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EditPwdForm {
	@NotNull(message = "原密码不能为空")
	private String orginalPassword;
	@NotBlank(message = "新密码不能为空")
	private String newPassword;
	@NotBlank(message = "确认密码不能为空")
	private String confirmPassword;

}
