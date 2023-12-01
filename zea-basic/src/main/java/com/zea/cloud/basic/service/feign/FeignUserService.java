package com.zea.cloud.basic.service.feign;

import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.bean.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author jfzou
 * 1、feignClient接口 有参数在参数必须加@PathVariable("XXX")和 @RequestParam("XXX")
 * 2、feignClient返回值为复杂对象时其类型必须有无参构造函数。
 */
@FeignClient(value = "user-service")
@Component
public interface FeignUserService {

    /**
     * 新增用户信息
     * @return 用户id
     */
    @PostMapping("/zea-cloud/zea-user/user/addUser")
    Result<Integer> addUser(@RequestBody User user);

    @PostMapping("/zea-cloud/zea-user/user/selectAllUser")
    Result<List<User>> selectAllUser();

}
