package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClassEditForm {

    @NotNull(message = "班级id不能为空")
    private Integer id;

    @NotBlank(message = "班级名称不能为空")
    private String className;

    @NotBlank(message = "班级编号不能为空")
    private String classNo;
}
