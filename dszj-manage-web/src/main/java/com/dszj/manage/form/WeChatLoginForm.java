package com.dszj.manage.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhangsy
 * @description 微信loginFrom
 * @date 2019/2/27 14:04
 */
@Data
public class WeChatLoginForm {

    @NotBlank(message = "登录失败，请重试")
    private String code;
}
