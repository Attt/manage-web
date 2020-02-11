package com.dszj.manage.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AuthForm{
	@NotNull(message = "角色Id不能为空")
    private Integer roleId;
	private List<Integer> menuIds;
	
}
