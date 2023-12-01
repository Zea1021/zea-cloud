package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.common.mq.MQTag;
import com.zea.cloud.common.mq.MQTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MqProducerMain {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup("producer-test");
        producer.setNamesrvAddr("127.0.0.1:10213");
        try {
            producer.start();
            Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, "rocketmq message test".getBytes(StandardCharsets.UTF_8));
            // 发送消息并得到消息的发送结果，然后打印
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
            producer.shutdown();
        } catch (MQClientException | MQBrokerException | RemotingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
