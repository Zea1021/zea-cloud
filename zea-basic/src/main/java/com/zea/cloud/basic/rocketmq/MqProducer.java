package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.basic.config.RocketMqConfig;
import com.zea.cloud.basic.constant.MQTag;
import com.zea.cloud.basic.constant.MQTopic;
import com.zea.cloud.basic.util.SpringBeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequestMapping("MqProducer")
public class MqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("send")
    public void send(@RequestParam("message") String message) {
        RocketMqConfig rocketMqConfig = SpringBeanUtil.getBean(RocketMqConfig.class);
        Assert.notNull(rocketMqConfig, "rocketMqConfig没有配置！");
        String producerGroup = rocketMqConfig.getProducerGroup();
        String nameSrvAddr = rocketMqConfig.getNameServer();
        log.info("producerGroup: {}, namesrvAddr: {}", producerGroup, nameSrvAddr);

        // 方式1：
        String destination = String.format("%s:%s", MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG);
        rocketMQTemplate.convertAndSend(destination, message);
        log.info("MqProducer send: {}", message);

        // 方式2：
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(rocketMqConfig.getProducerGroup());
        producer.setNamesrvAddr(rocketMqConfig.getNameServer());
        try {
            producer.start();
            Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, message.getBytes(StandardCharsets.UTF_8));
            // 发送消息并得到消息的发送结果，然后打印
            SendResult sendResult = producer.send(msg);
            log.info("sendResult: {}", sendResult.toString());
            producer.shutdown();
        } catch (MQClientException | MQBrokerException | RemotingException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
