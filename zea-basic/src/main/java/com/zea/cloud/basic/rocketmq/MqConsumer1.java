package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.common.mq.MQTag;
import com.zea.cloud.common.mq.MQTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者监听器
 * <p>
 * 注意：使用@RocketMQMessageListener注册监听器时，要在启动类的@SpringBootApplication注解中加上扫描rocketmq命令，否则消费者监听器注册不上
 * 即：@SpringBootApplication(scanBasePackages = {"com.zea.cloud","org.apache.rocketmq"})
 * 原因（猜测）：@SpringBootApplication默认只会扫描注入主类 BasicApplication 所在的包的 bean，即"com.zea.cloud"，而不会扫描注入"org.apache.rocketmq"的 bean
 * 2、注意：消费者定义时必须满足订阅关系一致性。消费者的订阅关系一致性指的是同一个消费者 Group 下所有 Consumer 实例所订阅的 Topic 、Tag 必须完全一致。
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "producer-test", topic = MQTopic.ZEA_TOPIC, selectorExpression = MQTag.SYNC_TAG, consumeMode = ConsumeMode.CONCURRENTLY)
public class MqConsumer1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        log.info("MqConsumer1:receive message: {}", msg);
    }

}
