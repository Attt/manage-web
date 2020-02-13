package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StudentAddForm {
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @NotBlank(message = "学生编号不能为空")
    private String studentNo;

    @NotNull(message = "学生性别不能为空")
    private Integer gender;

    @NotNull(message = "班级信息不能为空")
    private Integer classId;
}
