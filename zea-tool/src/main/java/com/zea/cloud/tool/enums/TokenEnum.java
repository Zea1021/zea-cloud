package com.zea.cloud.tool.enums;

import lombok.Getter;

@Getter
public enum TokenEnum {

    ACCESS_TOKEN("access_token", 3600 * 2, "访问令牌"),
    REFRESH_TOKEN("refreshToken", 3600 * 24, "刷新令牌"),

    ;

    /**
     * token类型
     */
    private final String type;
    /**
     * 过期时间，单位：秒
     */
    private final int expire;
    /**
     * 描述
     */
    private final String desc;

    TokenEnum(String type, int expire, String desc) {
        this.type = type;
        this.expire = expire;
        this.desc = desc;
    }
}
