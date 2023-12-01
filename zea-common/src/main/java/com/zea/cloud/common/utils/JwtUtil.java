package com.zea.cloud.common.utils;

import com.zea.cloud.common.bean.entity.User;

public class JwtUtil {

    public static User getUserInfo(String token) {
        return new User();
    }

    public static boolean verifyAccessToken(String accessToken) {
        return true;
    }
}
