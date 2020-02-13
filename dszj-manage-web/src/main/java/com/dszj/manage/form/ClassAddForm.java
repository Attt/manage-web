package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClassAddForm {

    @NotBlank(message = "班级姓名不能为空")
    private String className;

    @NotBlank(message = "班级编号不能为空")
    private String classNo;
}
