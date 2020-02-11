package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.entity.Dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DictQueryForm extends PageForm<Dict> {
	@DynamicWhere
    private String label;
	@DynamicWhere
    private String title;
}
