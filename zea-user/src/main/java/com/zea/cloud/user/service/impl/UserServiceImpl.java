package com.zea.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.user.mapper.UserMapper;
import com.zea.cloud.user.service.UserService;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


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
}
