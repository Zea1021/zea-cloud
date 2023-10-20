package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.basic.constant.MQTag;
import com.zea.cloud.basic.constant.MQTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者监听器
 * <p>
 * 注意：使用@RocketMQMessageListener注册监听器时，要在启动类的@SpringBootApplication注解中加上扫描rocketmq命令，否则消费者监听器注册不上
 * 即：@SpringBootApplication(scanBasePackages = {"com.zea.cloud","org.apache.rocketmq"})
 * 原因（猜测）：@SpringBootApplication默认只会扫描注入主类 BasicApplication 所在的包的 bean，即"com.zea.cloud"，而不会扫描注入"org.apache.rocketmq"的 bean
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "producer-test", topic = MQTopic.ZEA_TOPIC, selectorExpression = MQTag.SYNC_TAG)
public class MqConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        log.info("receive message: {}", msg);
    }

}
