package com.zea.cloud.common.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource(name = "MyRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 设置键值对
     * @param key 键
     * @param value 值
     */
    public <T> void setKeyValue(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     *
     * @param key 键
     * @param value 键
     * @param timeout 时间大小
     * @param unit 时间单位
     */
    public <T> void setKeyValue(final String key, final T value, final long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取缓存的值
     * @param key 键
     * @return 值
     */
    public <T> T getKeyValue(final String key) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 批量设置hash map键值对
     * @param key 键
     * @param map map对象
     */
    public <T, K> void setHashValue(final String key, final Map<T, K> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 单个设置hash map键值对
     * @param key 键
     * @param hashKey entry 的 key
     * @param value   entry 的 value
     */
    public <T, K> void setHashValue(final String key, final T hashKey, final K value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取hash map
     * @param key 键
     * @return hash map
     */
    public <T, K> Map<T, K> getHashValue(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hash map单个field的值
     * @param key 键
     * @return hash map
     */
    public <T, K> K getHashValue(final String key, final T hashKey) {
        HashOperations<String, T, K> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    /**
     * 批量从右侧设置list value
     * @param key 键
     * @param valueList list
     * @return 插入成功的数量
     * @param <T> 数据类型
     */
    public <T> Long setListValue(final String key, final List<T> valueList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, valueList);
        return count == null ? 0 : count;
    }

    /**
     * 单个从右侧设置list value
     * @param key 键
     * @param value value
     * @return 插入成功的数量
     * @param <T> 数据类型
     */
    public <T> Long setListValue(final String key, final T value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 获取list
     * @param key 键
     * @return list
     * @param <T> 数据类型
     */
    public <T> List<T> getListValue(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 设置set value
     * @param key 键
     * @param value z值
     * @param <T> 数据类型
     */
    public <T> Long setSetValue(final String key, final List<T> value) {
        Long count = redisTemplate.opsForSet().add(key, value.toArray());
        return count == null ? 0 : count;
    }

    /**
     * 设置set value
     * @param key 键
     * @param value z值
     * @param <T> 数据类型
     */
    public <T> Long setSetValue(final String key, final Set<T> value) {
        Long count = redisTemplate.opsForSet().add(key, value.toArray());
        return count == null ? 0 : count;
    }

    /**
     * 设置set value
     * @param key 键
     * @param value z值
     * @param <T> 数据类型
     */
    public <T> Long setSetValue(final String key, final T value) {
        Long count = redisTemplate.opsForSet().add(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 获取set
     * @param key 键
     * @return set
     * @param <T> 数据类型
     */
    public <T> Set<T> getSetValue(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断是否在set中
     * @param key 键
     * @param obj 判断对象
     * @return Boolean
     * @param <T> 数据类型
     */
    public <T> Boolean isMember(final String key, final T obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * 给key设置过期时间
     * @param key 键
     * @param timeout 时间大小（默认为毫秒）
     * @return 成功与否
     */
    public Boolean setExpire(final String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 给key设置过期时间
     * @param key 键
     * @param timeout 时间大小
     * @param unit 时间单位
     * @return 成功与否
     */
    public Boolean setExpire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取key的剩余过期时间
     * @param key 键
     * @return 剩余过期时间(单位为秒)
     */
    public Long getExpireTime(final String key) {
        return redisTemplate.getExpire(key, TimeUnit.MICROSECONDS);
    }

    /**
     * 获取key的剩余过期时间
     * @param key 键
     * @return 剩余过期时间
     */
    public Long getExpireTime(final String key, final TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 获取所有的key
     * @param pattern 匹配模式
     * @return redis中所有的key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除key
     * @param key 键
     * @return 删除成功
     */
    public Boolean deleteKey(final String key) {
        return redisTemplate.delete(key);
    }

}
