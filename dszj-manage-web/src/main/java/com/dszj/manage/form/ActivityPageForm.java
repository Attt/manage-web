package com.dszj.manage.form;

import com.dszj.manage.base.DynamicWhere;
import com.dszj.manage.base.OrderBy;
import com.dszj.manage.base.PageForm;
import com.dszj.manage.base.SortEnum;
import com.dszj.manage.entity.Activity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@OrderBy(field = "createTime",sort = {SortEnum.Desc})
public class ActivityPageForm extends PageForm<Activity> {
    @DynamicWhere
    private Integer creatorId;
}
