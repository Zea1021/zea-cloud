package com.zea.cloud.common.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginCondition {
    /**
     * 用户名
     */
    @NotBlank(message = "{login.username.is.blank}")
    private String userName;;

    /**
     * 密码
     */
    @NotBlank(message = "{login.password.is.blank}")
    private String password;

    /**
     * 图片验证码
     */
    @NotBlank(message = "{login.code.is.blank}")
    private String code;

    /**
     * 随机字符串，作为图片验证码在redis中的key的一部分
     */
     @NotBlank(message = "{login.random.string.is.blank}")
    private String randomStr;
}
