package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BindInfoForm {

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    private Integer id;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Integer memberId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 学号
     */
    private String studentNo;

}
