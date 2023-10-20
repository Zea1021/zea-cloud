package com.zea.cloud.basic.service.impl;

import com.zea.cloud.basic.bean.entity.User;
import com.zea.cloud.basic.service.TestService;
import com.zea.cloud.basic.service.feign.FeignUserService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private FeignUserService feignUserService;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void test() {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        User user = new User();
        user.setUserName("admin");
        user.setNickName("admin");
        user.setPassword("123456");
        user.setCreateTime(new Date());
        Integer user_id = feignUserService.addUser(user);
        System.out.println(user_id);
        try {
            int x = 1/0;
        }catch (ArithmeticException e)  {
            throw new RuntimeException(e);
        }

    }
}
