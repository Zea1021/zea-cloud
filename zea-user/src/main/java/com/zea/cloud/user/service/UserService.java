package com.zea.cloud.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.user.bean.entiry.User;

import java.util.List;

/**
 * 用户服务层
 * @author jfzou
 */
public interface UserService {

    /**
     * 新增用户
     * @param user 用户信息
     * @return user_id
     */
    Integer addUser(User user);

    /**
     * 查询所有用户信息
     * @return 用户信息列表
     */
    List<User> selectAllUser();

    /**
     * 分页查询用户信息
     * @param condition 查询条件
     * @return 用户信息列表
     */
    IPage<User> selectUserList(UserCondition condition);

    /**
     * 通过id查询用户信息
     * @param id 用户id
     * @return 用户信息
     */
    User selectUserInfoById(Integer id);

    void test();
}
