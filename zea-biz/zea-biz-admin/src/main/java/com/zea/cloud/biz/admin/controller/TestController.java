package com.zea.cloud.biz.admin.controller;

import com.zea.cloud.biz.admin.entity.User;
import com.zea.cloud.biz.admin.feign.FeignUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private FeignUserService feignUserService;

    @GetMapping("/testFeign")
    public void testFeign(){
        List<User> users = feignUserService.selectAllUser();
        for (User user : users) {
            log.info("user info: {}", user.toString());
        }
    }

}
