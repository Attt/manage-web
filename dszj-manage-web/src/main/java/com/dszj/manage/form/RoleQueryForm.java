package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleQueryForm extends PageForm<Role> {
	@DynamicWhere
    private String title;
}
