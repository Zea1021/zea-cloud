package com.zea.cloud.basic.config;

import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Slf4j
public class MinioConfig {

    @Value("${minio.endPoint}")
    private  String endpoint;

    @Value("${minio.accessKey}")
    private  String accessKey;

    @Value("${minio.secretKey}")
    private  String secretKey;

    @Bean
    public MinioClient MinioClient(){
        log.info("minio服务地址：" + endpoint);
        return new MinioClient.Builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}
