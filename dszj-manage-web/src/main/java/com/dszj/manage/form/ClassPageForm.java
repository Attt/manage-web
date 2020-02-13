package com.dszj.manage.form;

import com.dszj.manage.base.OrderBy;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.base.SortEnum;
import com.dszj.manage.entity.Class;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@OrderBy(field = "createTime",sort = {SortEnum.Desc})
public class ClassPageForm extends PageForm<Class> {
}
