package com.zea.cloud.tool.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象转map
 */
public class Obj2MapUtil {

    public static Map<String, Object> objectToMap(Object obj, Class<?> clazz) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取对象的所有的字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static Object mapToObj(Map<String, Object> map, Class<?> clazz) {
        return BeanUtil.mapToBean(map, clazz, true);
    }
}
