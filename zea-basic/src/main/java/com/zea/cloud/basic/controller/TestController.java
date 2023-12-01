package com.zea.cloud.basic.controller;

import com.zea.cloud.basic.service.TestService;
import com.zea.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private TestService testService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("testSeata")
    public void testSeata() {
        testService.testSeata();
    }

    @GetMapping("testRedis")
    public void testRedis() {
        // string
        redisUtil.setKeyValue("name", "zea", 30, TimeUnit.SECONDS);
        redisUtil.setKeyValue("age", 25, 30, TimeUnit.SECONDS);
        redisUtil.setKeyValue("gender", "male", 30, TimeUnit.SECONDS);
        System.out.println("name:" + redisUtil.getKeyValue("name"));

        // hash
        redisUtil.setHashValue("user", "name", "zea");
        redisUtil.setHashValue("user", "age", 25);
        redisUtil.setHashValue("user", "gender", "male");
        redisUtil.setExpire("user", 30, TimeUnit.SECONDS);
        System.out.println("user:" + redisUtil.getHashValue("user"));
        System.out.println("user:name:" + redisUtil.getHashValue("user", "name"));

        // list
        redisUtil.setListValue("userList", 10);
        redisUtil.setListValue("userList", 15);
        redisUtil.setListValue("userList", 20);
        redisUtil.setExpire("userList", 30, TimeUnit.SECONDS);
        System.out.println("userList:" + redisUtil.getListValue("userList"));

        // set1
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        redisUtil.setSetValue("set1", list);
        // set2
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        redisUtil.setSetValue("set2", set);
        // set3
        redisUtil.setSetValue("set3", 1);
        redisUtil.setSetValue("set3", 2);
        redisUtil.setSetValue("set3", 3);
        // setExpire
        redisUtil.setExpire("set1", 30000);
        redisUtil.setExpire("set2", 30000);
        redisUtil.setExpire("set3", 30000);
        // check
        System.out.println(redisUtil.isMember("set1", 1));
        System.out.println(redisUtil.isMember("set2", 1));
        System.out.println(redisUtil.isMember("set3", 1));
        System.out.println("set1:" + redisUtil.getSetValue("set1"));
        System.out.println("set2:" + redisUtil.getSetValue("set2"));
        System.out.println("set3:" + redisUtil.getSetValue("set3"));

        // z-set todo
        System.out.println("all keys:" + redisUtil.keys("*"));
    }
}
