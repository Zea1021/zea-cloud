package com.zea.cloud.common.bean.entity;

import com.zea.cloud.common.bean.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserInfo extends UserVo {

    private String accessToken;

    private String refreshToken;
}