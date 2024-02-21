package com.zea.cloud.common.utils;

import com.zea.cloud.common.bean.entity.LoginUserInfo;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.session.Session;
import com.zea.cloud.common.session.SessionManage;
import com.zea.cloud.tool.constant.HttpConstant;
import com.zea.cloud.tool.utils.JwtUtil;
import com.zea.cloud.tool.utils.SpringBeanUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * 登录用户上下文工具类
 */
public class UserContextUtil {

    private final static String USER_SESSION_PREFIX = "UserSession:%s:%s";

    /**
     * 获取redis中缓存的当前登录用户信息
     * @return   当前登录用户信息
     */
    public static LoginUserInfo getCurrentUserInfo() {
        Session session = getCurrentSessionFromRequest();
        if (session == null) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        Object object = session.get(Session.LOGIN_USER_INFO);
        if (object == null) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        return (LoginUserInfo) object;
    }

    /**
     * 保存当前登录用户session信息
     * @param loginUserInfo  当前登录用户信息，含有token
     */
    public static void saveCurrentUserSession(LoginUserInfo loginUserInfo) {
        Session session = new Session();
        session.set(Session.LOGIN_USER_INFO, loginUserInfo);
        String cacheKey = getSessionKeyByToken(loginUserInfo.getAccessToken());
        SessionManage sessionManage = SpringBeanUtil.getBean(SessionManage.class);
        assert sessionManage != null;
        sessionManage.saveSession(cacheKey, session);
    }

    /**
     * 获取当前session
     * @return        session
     */
    private static Session getCurrentSessionFromRequest() {
        String cacheKey = getSessionKeyByRequest();
        SessionManage sessionManage = SpringBeanUtil.getBean(SessionManage.class);
        assert sessionManage != null;
        return sessionManage.getSession(cacheKey);
    }

    /**
     * 获取当前session
     * @return        session
     */
    public static Session getCurrentSessionFromToken(String token) {
        String cacheKey = getSessionKeyByToken(token);
        SessionManage sessionManage = SpringBeanUtil.getBean(SessionManage.class);
        assert sessionManage != null;
        return sessionManage.getSession(cacheKey);
    }

    /**
     * 刷新session，只延长session有效时长，不会改变session内容
     */
    public static void refreshSession() {
        String cacheKey = getSessionKeyByRequest();
        SessionManage sessionManage = SpringBeanUtil.getBean(SessionManage.class);
        assert sessionManage != null;
        sessionManage.refreshSession(cacheKey);
    }

    /**
     * 清除当前用户
     */
    public static void removeCurrentUser() {
        String cacheKey = getSessionKeyByRequest();
        SessionManage sessionManage = SpringBeanUtil.getBean(SessionManage.class);
        assert sessionManage != null;
        sessionManage.removeSession(cacheKey);
    }

    /**
     * 获取session存储在redis中的key
     * @return          session存储在redis中的key
     */
    private static String getSessionKeyByRequest() {
        HttpServletRequest request = ThreadLocalUtil.get(ThreadLocalUtil.HTTP_SERVLET_REQUEST);
        if (request == null) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(token)) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        token = token.replace(HttpConstant.BEARER + " ", "").trim();
        return getSessionKeyByToken(token);
    }

    /**
     * 获取session存储在redis中的key
     * @param token   token
     * @return        session存储在redis中的key
     */
    private static String getSessionKeyByToken(String token) {
        String clientId = JwtUtil.getClientId(token);
        String userId = JwtUtil.getUserId(token);
        return String.format(USER_SESSION_PREFIX, clientId, userId);
    }

}
