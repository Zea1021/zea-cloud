package com.zea.cloud.basic.rocketmq;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

@Component
public class MqProducerServer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通消息，没有返回确认
     * @param topic    主题
     * @param tag      标签
     * @param message  消息
     * @param <T>      消息类型
     */
    public <T> void sendMessage(String topic, String tag, T message) {
        String destination = String.format("%s:%s", topic, tag);
        rocketMQTemplate.convertAndSend(destination, message);
    }

}
