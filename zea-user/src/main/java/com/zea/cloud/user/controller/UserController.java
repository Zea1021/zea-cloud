package com.zea.cloud.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zea.cloud.common.annotation.ForbidRepeatClick;
import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.utils.ResultUtil;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制层
 * @author jfzou
 */
@RestController
@RequestMapping("user")
public class UserController {

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
    @ForbidRepeatClick()
    @PostMapping("addUser")
    public Result<Integer> addUser(@RequestBody User user) {
        Integer result = userService.addUser(user);
        return ResultUtil.success(result);
    }

    /**
     * 查询所有用户信息
     * @return 用户信息列表
     */
    @GetMapping("selectAllUser")
    public Result<List<User>> selectAllUser() {
        return ResultUtil.success(userService.selectAllUser());
    }

    /**
     * 分页查询用户信息
     * @param condition 查询条件
     * @return 用户信息列表
     */
    @PostMapping("selectUserList")
    public Result<IPage<User>> selectUserList(@RequestBody UserCondition condition) {
        return ResultUtil.success(userService.selectUserList(condition));
    }

    /**
     * 通过id查询用户信息
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("selectUserInfoById")
    public Result<User> selectUserInfoById(@RequestParam Integer id){
        return ResultUtil.success(userService.selectUserInfoById(id));
    }

    @GetMapping("test")
    public void test() {
        userService.test();
    }

}
