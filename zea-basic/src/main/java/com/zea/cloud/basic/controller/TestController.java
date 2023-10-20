package com.zea.cloud.basic.controller;

import com.zea.cloud.basic.bean.entity.User;
import com.zea.cloud.basic.service.TestService;
import com.zea.cloud.basic.service.feign.FeignUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("seata")
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("test")
    public void test() {
        testService.test();
    }

}
