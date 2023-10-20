package com.zea.cloud.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.user.bean.entiry.User;
import com.zea.cloud.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制层
 * @author jfzou
 */
@RestController
@RequestMapping("user")
public class UserController implements InitializingBean {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 新增用户
     * @param user 用户信息
     * @return user_id
     */
    @PostMapping("addUser")
    public Integer addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 查询所有用户信息
     * @return 用户信息列表
     */
    @GetMapping("selectAllUser")
    public List<User> selectAllUser() {
        return userService.selectAllUser();
    }

    /**
     * 分页查询用户信息
     * @param condition 查询条件
     * @return 用户信息列表
     */
    @PostMapping("selectUserList")
    public IPage<User> selectUserList(@RequestBody UserCondition condition) {
        return userService.selectUserList(condition);
    }

    /**
     * 通过id查询用户信息
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("selectUserInfoById")
    public User selectUserInfoById(@RequestParam Integer id){
        return userService.selectUserInfoById(id);
    }

    @GetMapping("test")
    public void test() {
        userService.test();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("================UserController Init Success!=================");
    }
}
