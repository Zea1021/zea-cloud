package com.zea.cloud.biz.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zea
 */
@EnableFeignClients(basePackages = {"com.zea.cloud"})
@SpringBootApplication
public class AdminBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminBizApplication.class, args);
        System.out.println("AdminBizApplication启动成功");
    }
}
