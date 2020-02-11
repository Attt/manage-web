package com.dszj.manage.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserEditForm {
	@NotNull(message = "用户ID不能为空")
	private Integer id;
	@NotNull(message = "部门不能为空")
	private Integer deptId;
	@NotBlank(message = "用户名不能为空")
	private String username;
	@NotBlank(message = "昵称不能为空")
	private String nickname;
	private Integer sex;
	private Integer status;
	@NotBlank(message = "手机号不能为空")
	private String phone;
	private String email;
	private String remark;
	private List<Integer> roleIds;

}
