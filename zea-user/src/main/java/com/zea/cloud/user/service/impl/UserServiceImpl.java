package com.zea.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zea.cloud.common.bean.LoginCondition;
import com.zea.cloud.common.bean.entity.LoginUserInfo;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.session.Session;
import com.zea.cloud.common.utils.RedisUtil;
import com.zea.cloud.common.utils.UserContextUtil;
import com.zea.cloud.tool.enums.TokenEnum;
import com.zea.cloud.tool.utils.JwtUtil;
import com.zea.cloud.tool.utils.Md5Util;
import com.zea.cloud.user.bean.condition.RefreshCondition;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.user.mapper.UserMapper;
import com.zea.cloud.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 用户服务实现
 * @author jfzou
 * @since 2024/1/4
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String VERIFY_CODE_PRE = "verify_code:";

    @Value("${verify.code.enable}")
    private Boolean verifyCodeEnable;

    @Value("${client.id}")
    private String clientId;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    /**
     * 新增用户
     * @param user 用户信息
     * @return user_id
     */
    @Override
    public Integer addUser(User user) {
        // 将用户密码进行加密存储
        user.setPassword(Md5Util.encodeByMD5(user.getPassword()));
        userMapper.insert(user);
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
    public User selectUserInfoByName(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 登录控制器
     * @param condition  登录信息
     * @return           用户信息 + token
     */
    @Override
    public LoginUserInfo login(LoginCondition condition) {
        // 校验图片验证码（防止机器人攻击）
        if (verifyCodeEnable) {
            String codeKey = VERIFY_CODE_PRE + condition.getRandomStr();
            String code = redisUtil.getKeyValue(codeKey).toString();
            if (!StringUtils.hasLength(code)) {
                throw new MyException(ErrorCode.VALID_EXCEPTION, "verify code is expired");
            }
            if (!condition.getCode().equals(code)) {
                throw new MyException(ErrorCode.VALID_EXCEPTION, "verify code is error");
            }
        }
        // 查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, condition.getUserName()));
        // 校验用户登录信息
        if (user == null || !user.getPassword().equals(Md5Util.encodeByMD5(condition.getPassword()))) {
            throw new MyException(ErrorCode.NOT_PERMISSION, "用户名或密码错误");
        }
        // 构造用户登录信息(含token)
        LoginUserInfo loginUserInfo = doUserLogin(user);
        // 保存用户session信息到redis，有效期两小时
        UserContextUtil.saveCurrentUserSession(loginUserInfo);
        return loginUserInfo;
    }

    /**
     * 刷新token
     * @param condition  刷新条件
     * @return           用户信息 + token
     */
    @Override
    public LoginUserInfo refreshToken(RefreshCondition condition) {
        String refreshToken = condition.getRefreshToken();
        // 校验refreshToken及其类型
        if (!JwtUtil.verifyToken(refreshToken) ||
                !JwtUtil.getTokenType(refreshToken).equals(TokenEnum.REFRESH_TOKEN.getType())) {
            throw new MyException(ErrorCode.REFRESH_TOKEN_IS_FAILED);
        }
        // 判断session是否过期
        Session session = UserContextUtil.getCurrentSessionFromToken(refreshToken);
        if (session == null || CollectionUtils.isEmpty(session.getSessionCache())) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        // 查询用户信息
        String userId = JwtUtil.getUserId(refreshToken);
        User user = selectUserInfoById(Integer.parseInt(userId));
        // 构造用户登录信息及其token
        LoginUserInfo loginUserInfo = doUserLogin(user);
        // 原有的refreshToken不变
        loginUserInfo.setRefreshToken(condition.getRefreshToken());
        // 保存用户session信息
        UserContextUtil.saveCurrentUserSession(loginUserInfo);
        return loginUserInfo;
    }

    /**
     * 构造用户登录信息和token
     * @param user  用户信息
     * @return      用户登录信息和token
     */
    private LoginUserInfo doUserLogin(User user) {
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtils.copyProperties(user, loginUserInfo);
        String accessToken = JwtUtil.createToken(
                clientId, user.getId().toString(), user.getUserName(), TokenEnum.ACCESS_TOKEN);
        String refreshToken = JwtUtil.createToken(
                clientId, user.getId().toString(), user.getUserName(), TokenEnum.REFRESH_TOKEN);
        loginUserInfo.setAccessToken(accessToken);
        loginUserInfo.setRefreshToken(refreshToken);
        return loginUserInfo;
    }

}
