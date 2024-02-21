package com.zea.cloud.common.session;

import com.zea.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Session 管理，操作
 */
@Component
public class SessionManage {

    /**
     * 过期时间
     */
    private final static Integer DEFAULT_TIMEOUT = 60 * 2;

    @Resource
    private RedisUtil redisUtil;

    /**
     *
     * @param key      key
     * @param session  session
     */
    public void saveSession(String key, Session session) {
        redisUtil.setKeyValue(key, session, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    /**
     * 获取session
     * @param key   key
     * @return      session
     */
    public Session getSession(String key){
        return (Session) redisUtil.getKeyValue(key);
    }

    /**
     * session
     * @param key  key
     */
    public void removeSession(String key) {
        redisUtil.deleteKey(key);
    }

    /**
     * 延迟刷新session有效期，延长两小时
     * @param key  key
     */
    public void refreshSession(String key){
        redisUtil.setExpire(key, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

}
