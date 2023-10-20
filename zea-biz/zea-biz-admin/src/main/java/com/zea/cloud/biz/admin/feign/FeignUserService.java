package com.zea.cloud.biz.admin.feign;

import com.zea.cloud.biz.admin.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author jfzou
 */
@FeignClient(value = "user-service")
@Component
public interface FeignUserService {

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @PostMapping("/zea-cloud/zea-user/user/addUser")
    Integer addUser(@RequestBody User user);

    @GetMapping("/zea-cloud/zea-user/user/selectAllUser")
    List<User> selectAllUser();
}
