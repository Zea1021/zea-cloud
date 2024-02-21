package com.zea.cloud.common.utils;

import java.util.*;

public class ThreadLocalUtil<T> {

    public final static String HTTP_SERVLET_REQUEST = "httpServletRequest";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(4);
        }
    };


    public static Map<String, Object> getThreadLocal(){
        return THREAD_LOCAL.get();
    }

    public static <T> T get(String key) {
        Map map = THREAD_LOCAL.get();
        return (T) map.get(key);
    }

    public static <T> T get(String key,T defaultValue) {
        Map map = THREAD_LOCAL.get();
        return (T) map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    public static void set(String key, Object value) {
        Map map = THREAD_LOCAL.get();
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map map = THREAD_LOCAL.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static <T> Map<String,T> fetchVarsByPrefix(String prefix) {
        Map<String,T> vars = new HashMap<>();
        if( prefix == null ){
            return vars;
        }
        Map map = THREAD_LOCAL.get();
        Set<Map.Entry> set = map.entrySet();

        for( Map.Entry entry : set){
            Object key = entry.getKey();
            if( key instanceof String ){
                if( ((String) key).startsWith(prefix) ){
                    vars.put((String)key,(T)entry.getValue());
                }
            }
        }
        return vars;
    }

    public static <T> T remove(String key) {
        Map map = THREAD_LOCAL.get();
        return (T)map.remove(key);
    }

    public static void clear(String prefix) {
        if( prefix == null ){
            return;
        }
        Map map = THREAD_LOCAL.get();
        Set<Map.Entry> set = map.entrySet();
        List<String> removeKeys = new ArrayList<>();

        for( Map.Entry entry : set ){
            Object key = entry.getKey();
            if( key instanceof String ){
                if( ((String) key).startsWith(prefix) ){
                    removeKeys.add((String)key);
                }
            }
        }
        for( String key : removeKeys ){
            map.remove(key);
        }
    }
}