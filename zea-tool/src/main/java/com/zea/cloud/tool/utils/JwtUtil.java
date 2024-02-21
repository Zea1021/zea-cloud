package com.zea.cloud.tool.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zea.cloud.tool.constant.HttpConstant;
import com.zea.cloud.tool.enums.TokenEnum;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "zea1021";
    private static final String CLIENT_ID = "clientId";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String TYPE = "type";


    /**
     * 创建token
     * @param userId       用户id
     * @param tokenEnum    token枚举类型：accessToken，refreshToken
     * @return             token
     */
    public static String createToken(String clientId, String userId, String userName, TokenEnum tokenEnum) {
        DateTime now = DateUtil.date();
        DateTime newTime = now.offsetNew(DateField.SECOND, tokenEnum.getExpire());
        Map<String, Object> map = new HashMap<>(8);
        // 客户端id
        map.put(CLIENT_ID, clientId);
        // 用户id
        map.put(USER_ID, userId);
        // 用户名称
        map.put(USER_NAME, userName);
        // token类型
        map.put(TYPE, tokenEnum.getType());
        // 签发时间
        map.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        map.put(JWTPayload.EXPIRES_AT, newTime);
        // 生效时间
        map.put(JWTPayload.NOT_BEFORE, now);
        return JWTUtil.createToken(map, SECRET.getBytes());
    }

    public static String getClientId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        // 获取载荷部分
        JWTPayload payload = jwt.getPayload();
        return payload.getClaim(CLIENT_ID).toString();
    }

    public static String getUserId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        // 获取载荷部分
        JWTPayload payload = jwt.getPayload();
        return payload.getClaim(USER_ID).toString();
    }

    public static String getUserName(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        // 获取载荷部分
        JWTPayload payload = jwt.getPayload();
        return payload.getClaim(USER_NAME).toString();
    }

    public static String getTokenType(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        // 获取载荷部分
        JWTPayload payload = jwt.getPayload();
        return payload.getClaim(TYPE).toString();
    }

    public static boolean verifyToken(String token) {
        if (StringUtils.hasLength(token)) {
            // 如果token含有 bearer 前缀，则去掉
            token =  token.replace(HttpConstant.BEARER + " ", "");
            try {
                // 验证算法
                JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256(SECRET.getBytes()));
                // 验证时间，如验证token是否过期
                JWTValidator.of(token).validateDate(DateUtil.date());
            } catch (Exception ex) {
                return false;
            }
            // JWTUtil.verify 只能验证JWT Token的签名是否有效，其他payload字段验证都可以使用JWTValidator完成
            return JWTUtil.verify(token, SECRET.getBytes());
        }
        return false;
    }

}
