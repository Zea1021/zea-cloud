package com.zea.cloud.common.session;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Session对象
 */
@Data
public class Session implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String LOGIN_USER_INFO = "login_user_info";

    // 缓存
    private final Map<String, Object> sessionCache = new HashMap<>(4);

    public void set(String key, Object object){
        sessionCache.put(key,object);
    }

    public Object get(String key){
        return sessionCache.get(key);
    }

    public Session() {
    }

}
