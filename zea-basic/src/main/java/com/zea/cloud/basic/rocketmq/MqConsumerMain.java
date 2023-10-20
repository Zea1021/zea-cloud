package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.basic.constant.MQTag;
import com.zea.cloud.basic.constant.MQTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

@Slf4j
public class MqConsumerMain {

    public static void main(String[] args) {
        // 通过push模式消费消息，指定消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-test");
        // 指定NameServer的地址
        consumer.setNamesrvAddr("127.0.0.1:10213");

        // 订阅这个topic下的所有的消息
        try {
            consumer.subscribe(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG);
            // 注册一个消费的监听器，当有消息的时候，会回调这个监听器来消费消息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                for (MessageExt msg : msgList) {
                    System.out.println("消费消息: " + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            // 启动消费者
            consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Consumer Started.");
    }
}
