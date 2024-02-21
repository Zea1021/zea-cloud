package com.zea.cloud.user.oauth.service;

import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.user.oauth.bean.entity.SecurityUser;
import com.zea.cloud.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取用户信息
        User user = userService.selectUserInfoByName(username);
        if(user == null){
            throw new MyException(ErrorCode.UN_AUTH, "Username not found");
        }

        // 获取用户角色权限
        List<SimpleGrantedAuthority> rolesToAuthorities = null;
        if (StringUtils.hasLength(user.getRoles())) {
            rolesToAuthorities = Arrays.stream(user.getRoles().split(","))
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        // 数据库没有配置权限，则默认权限为普通人员
        if (CollectionUtils.isEmpty(rolesToAuthorities)) {
            rolesToAuthorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        }

        // 返回 UserDetails
        return SecurityUser.builder()
                .id(String.valueOf(user.getId()))
                .userName(user.getUserName())
                // 前缀{noop}表示登陆密码无需加密,因为数据库已经使用md5加密过了
                .password("{noop}" + user.getPassword())
                .isEnabled(user.getIsEnabled())
                .authorities(rolesToAuthorities)
                .build();
    }

}