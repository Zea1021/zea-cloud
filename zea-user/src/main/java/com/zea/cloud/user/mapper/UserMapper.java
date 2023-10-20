package com.zea.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zea.cloud.user.bean.condition.UserCondition;
import com.zea.cloud.user.bean.entiry.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

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
    IPage<User> selectUserList(Page<User> page, @Param("condition") UserCondition condition);

    /**
     * 通过id查询用户信息
     * @param id 用户id
     * @return 用户信息
     */
    User selectUserInfoById(@Param("id") Integer id);

}
