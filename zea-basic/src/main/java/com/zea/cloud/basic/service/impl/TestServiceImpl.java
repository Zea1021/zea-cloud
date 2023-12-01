package com.zea.cloud.basic.service.impl;

import com.zea.cloud.basic.service.TestService;
import com.zea.cloud.basic.service.feign.FeignUserService;
import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private FeignUserService feignUserService;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void testSeata() {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        User user = new User();
        user.setUserName("admin");
        user.setNickName("admin");
        user.setPassword("123456");
        user.setCreateTime(new Date());
        Result<Integer> result = feignUserService.addUser(user);
        if (result == null || !result.getCode().equals(ErrorCode.SUCCESS.getCode())) {
            throw new MyException(ErrorCode.FEIGN_API_EXCEPTION, "调用User微服务新增用户失败！");
        }
        System.out.println(result.getData());
        try {
            int x = 1/0;
        }catch (ArithmeticException e)  {
            throw new RuntimeException(e);
        }

    }
}
