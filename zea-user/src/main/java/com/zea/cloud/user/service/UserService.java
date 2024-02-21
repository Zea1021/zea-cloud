package com.zea.cloud.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zea.cloud.common.bean.LoginCondition;
import com.zea.cloud.common.bean.entity.LoginUserInfo;
import com.zea.cloud.user.bean.condition.RefreshCondition;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.common.bean.entity.User;

import java.util.List;

/**
 * 用户服务层
 * @author jfzou
 * @since 2024/1/4
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

    /**
     * 通过id查询用户信息
     * @param userName  用户名称
     * @return          用户信息
     */
    User selectUserInfoByName(String userName);

    /**
     * 登录控制器
     * @param condition  登录信息
     * @return           用户信息 + token
     */
    LoginUserInfo login(LoginCondition condition);

    /**
     * 刷新token
     * @param condition  刷新条件
     * @return           用户信息 + token
     */
    LoginUserInfo refreshToken(RefreshCondition condition);
}
