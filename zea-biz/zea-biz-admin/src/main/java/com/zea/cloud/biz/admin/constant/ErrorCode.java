package com.zea.cloud.biz.admin.constant;

public enum ErrorCode {

    SUCCESS("0", "SUCCESS"),

    SYSTEM_EXCPTION("-1", "SYSTEM_EXCPTION"),

    BUSINESS_EXCEPTION("-2","business exception"),

    REFRESH_TOKEN_IS_FAILED("-3","refresh token is failed or timeout"),

    VALID_EXCEPTION("-4","validate exception"),

    FEIGN_API_EXCEPTION("-5","feign api exception"),

    NO_DATA_PERMISSION("-6","has no data permission"),

    EMAIL_SEND_ERROR("-18","Email send error"),

    UNAUTHROTIED("401","need to login"),
    NOTPERMISSION("403","has no permission"),
    ;

    private String index;
    private String desc;

    public String getIndex() {
        return index;
    }

    public String getDesc() {
        return desc;
    }

    ErrorCode(String index, String desc) {
        this.index = index;
        this.desc = desc;
    }

}
