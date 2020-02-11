package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ActivityAddForm {
    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "类型不能为空")
    private Integer type;

    private String description;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须大于现在")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须大于现在")
    private Date endTime;

}
