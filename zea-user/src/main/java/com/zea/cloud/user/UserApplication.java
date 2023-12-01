package com.zea.cloud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jfzou
 */
@SpringBootApplication(scanBasePackages = {"com.zea.cloud"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("UserApplication启动成功");
    }
}
