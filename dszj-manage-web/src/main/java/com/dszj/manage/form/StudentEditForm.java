package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class StudentEditForm {
    @NotNull(message = "id不能为空")
    private Integer id;

    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @NotNull(message = "学生编号不能为空")
    private String studentNo;

    @NotNull(message = "班级信息不能为空")
    private Integer classId;
}
