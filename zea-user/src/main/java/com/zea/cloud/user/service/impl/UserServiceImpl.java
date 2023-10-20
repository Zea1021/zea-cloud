package com.zea.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.user.bean.entiry.User;
import com.zea.cloud.user.mapper.UserMapper;
import com.zea.cloud.user.service.UserService;
import com.zea.cloud.user.util.RedisUtil;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 用户服务实现
 * @author jfzou
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用户mapper层
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 新增用户
     * @param user 用户信息
     * @return user_id
     */
    @Override
    public Integer addUser(User user) {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        userMapper.insert(user);
        System.out.println(user.toString());
        return user.getId();
    }

    /**
     * 查询所有用户信息
     * @return 用户信息列表
     */
    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    /**
     * 分页查询用户信息
     * @param condition 查询条件
     * @return 用户信息列表
     */
    @Override
    public IPage<User> selectUserList(UserCondition condition) {
        Page<User> page = new Page<>(condition.getPageNum(), condition.getPageSize());
        return userMapper.selectUserList(page, condition);
    }

    /**
     * 通过id查询用户信息
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public User selectUserInfoById(Integer id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        return userMapper.selectOne(queryWrapper);
    }



    @Override
    public void test() {

    }

//    @Resource
//    private RedisUtil redisUtil;
//
//    @Override
//    public void test() {
//        // string
//        redisUtil.setKeyValue("name", "zea", 30, TimeUnit.SECONDS);
//        redisUtil.setKeyValue("age", 25, 30, TimeUnit.SECONDS);
//        redisUtil.setKeyValue("gender", "male", 30, TimeUnit.SECONDS);
//
//        // hash
//        redisUtil.setHashValue("user", "name", "zea");
//        redisUtil.setHashValue("user", "age", 25);
//        redisUtil.setHashValue("user", "gender", "male");
//        redisUtil.setExpire("user", 30, TimeUnit.SECONDS);
//        System.out.println("user:" + redisUtil.getHashValue("user"));
//        System.out.println("user:name:" + (String) redisUtil.getHashValue("user", "name"));
//
//        // list
//        redisUtil.setListValue("userList", 10);
//        redisUtil.setListValue("userList", 15);
//        redisUtil.setListValue("userList", 20);
//        redisUtil.setExpire("userList", 30, TimeUnit.SECONDS);
//        System.out.println("userList:" + redisUtil.getListValue("userList"));
//
//        // set1
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        redisUtil.setSetValue("set1", list);
//        // set2
//        Set<Integer> set = new HashSet<>();
//        set.add(1);
//        set.add(2);
//        set.add(3);
//        redisUtil.setSetValue("set2", set);
//        // set3
//        redisUtil.setSetValue("set3", 1);
//        redisUtil.setSetValue("set3", 2);
//        redisUtil.setSetValue("set3", 3);
//        // setExpire
//        redisUtil.setExpire("set1", 30000);
//        redisUtil.setExpire("set2", 30000);
//        redisUtil.setExpire("set3", 30000);
//        // check
//        System.out.println(redisUtil.isMember("set1", 1));
//        System.out.println(redisUtil.isMember("set2", 1));
//        System.out.println(redisUtil.isMember("set3", 1));
//        System.out.println("set1:" + redisUtil.getSetValue("set1"));
//        System.out.println("set2:" + redisUtil.getSetValue("set2"));
//        System.out.println("set3:" + redisUtil.getSetValue("set3"));
//
//        // z-set
//
//
//        System.out.println("all keys:" + redisUtil.keys("*"));
//    }

}
