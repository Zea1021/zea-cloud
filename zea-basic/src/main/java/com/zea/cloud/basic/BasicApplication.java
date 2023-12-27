package com.zea.cloud.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注意：添加@SpringBootApplication(scanBasePackages = {"com.xxx.xxx"})和@MapperScan注解之后，在网页或者postman中做请求的时候会显示404错误。
 * 原因：@SpringBootApplication(scanBasePackages = {“com.xxx.xxx”})注解会导致 Controller 层无法扫描到
 * 解决：在@SpringBootApplication(scanBasePackages = {“com.xxx.xxx”})中添加 Controller 层的路径 或者 本项目的路径进去即可。目的是让其能够扫描到 Controller 层。
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.zea.cloud", "org.apache.rocketmq"})
public class BasicApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
        System.out.println("BasicApplication启动成功!");
    }
}