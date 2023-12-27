package com.zea.cloud.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS("0", "SUCCESS"),

    SYSTEM_EXCEPTION("-1", "SYSTEM_EXCEPTION"),

    BUSINESS_EXCEPTION("-2","Business exception"),

    REFRESH_TOKEN_IS_FAILED("-3","Refresh token is failed or timeout"),

    VALID_EXCEPTION("-4","Validate exception"),

    FEIGN_API_EXCEPTION("-5","Feign api exception"),

    NO_DATA_PERMISSION("-6","Has no data permission"),

    EMAIL_SEND_ERROR("-18","Email send error"),

    UN_AUTH("401","Need to login"),

    NOT_PERMISSION("403","Has no permission"),

    FORBID_REPEAT_CLICK("-7","Forbid repeat click"),
    ;

    private final String code;

    private final String desc;

    ErrorCode(String index, String desc) {
        this.code = index;
        this.desc = desc;
    }


}
