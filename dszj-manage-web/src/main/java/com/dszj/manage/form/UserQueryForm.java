package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.OptEnum;
import com.dszj.manage.base.OrderBy;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@OrderBy(field="createTime")
public class UserQueryForm extends PageForm<User> {
	@DynamicWhere(opt=OptEnum.Like)
    private String nickname;
	@DynamicWhere
	private Integer deptId;
	@DynamicWhere
	private Integer status;
}
