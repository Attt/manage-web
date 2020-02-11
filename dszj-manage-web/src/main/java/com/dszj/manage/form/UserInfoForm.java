package com.dszj.manage.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserInfoForm {
	@NotBlank(message = "昵称不能为空")
	private String nickname;
	private Integer sex;
	@NotBlank(message = "手机号不能为空")
	private String phone;
	private String email;

}
