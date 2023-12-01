package com.zea.cloud.basic.rocketmq;

import com.zea.cloud.basic.config.RocketMqConfig;
import com.zea.cloud.basic.service.feign.FeignUserService;
import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.mq.MQTag;
import com.zea.cloud.common.mq.MQTopic;
import com.zea.cloud.basic.util.SpringBeanUtil;
import com.zea.cloud.common.utils.ResultUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("MqProducer")
public class MqProducer {

    @Resource
    private MqProducerService mqProducerService;

    @Resource
    private FeignUserService feignUserService;

    @GetMapping("send")
    public void send(@RequestParam(name = "message") String message) {
        // 方式1
        mqProducerService.sendMessage(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, message);
        log.info("send message: {}", message);
    }

    @GetMapping("sendMessage")
    public Result<?> sendMessage(@RequestParam(name = "message") String message) {
        RocketMqConfig rocketMqConfig = SpringBeanUtil.getBean(RocketMqConfig.class);
        if (rocketMqConfig == null) {
            throw new MyException(ErrorCode.SYSTEM_EXCEPTION, "rocketMqConfig没有配置！");
        }
        String producerGroup = rocketMqConfig.getProducerGroup();
        String nameSrvAddr = rocketMqConfig.getNameServer();
        log.info("producerGroup: {}, namesrvAddr: {}", producerGroup, nameSrvAddr);

        // 方式1
        mqProducerService.sendMessage(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, message);
        log.info("send message: {}", message);

        // 方式2
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameSrvAddr);
        try {
            // 生产消息
            List<Message> messages = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, "test002", (message + i).getBytes(StandardCharsets.UTF_8));
                messages.add(msg);
            }
            // 启动生产者服务
            producer.start();
            // 发送消息并得到消息的发送结果
            SendResult sendResult = producer.send(messages);
            // 关闭生产者服务
            producer.shutdown();
            log.info("send message: {}", sendResult.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResultUtil.success();
    }

    @GetMapping("sendDelayMessage")
    public Result<?> sendDelayMessage(@RequestParam(name = "message") String message) {
        RocketMqConfig rocketMqConfig = SpringBeanUtil.getBean(RocketMqConfig.class);
        if (rocketMqConfig == null) {
            throw new MyException(ErrorCode.SYSTEM_EXCEPTION, "rocketMqConfig没有配置！");
        }
        String producerGroup = rocketMqConfig.getProducerGroup();
        String nameSrvAddr = rocketMqConfig.getNameServer();
        log.info("producerGroup: {}, namesrvAddr: {}", producerGroup, nameSrvAddr);

        // new 一个 DefaultMQProducer并配置相关信息
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(rocketMqConfig.getProducerGroup());
        producer.setNamesrvAddr(rocketMqConfig.getNameServer());
        try {
            Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, "test002", message.getBytes(StandardCharsets.UTF_8));
            // level 默认 = 0,消息为非延迟消息; level = 1,消息为1s的延迟消息; level >= 18，消息为2h的延迟消息;
            // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            msg.setDelayTimeLevel(2); // 5s
            producer.start();
            SendResult sendResult = producer.send(msg);
            producer.shutdown();
            log.info("send message: {}", sendResult.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultUtil.success();
    }

    @GetMapping("sendOrderMessage")
    public Result<?> sendOrderMessage(@RequestParam(name = "message") String message) {
        RocketMqConfig rocketMqConfig = SpringBeanUtil.getBean(RocketMqConfig.class);
        if (rocketMqConfig == null) {
            throw new MyException(ErrorCode.SYSTEM_EXCEPTION, "rocketMqConfig没有配置！");
        }
        String producerGroup = rocketMqConfig.getProducerGroup();
        String nameSrvAddr = rocketMqConfig.getNameServer();
        log.info("producerGroup: {}, namesrvAddr: {}", producerGroup, nameSrvAddr);

        // 构建生产者
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameSrvAddr);
        // 构建消息:添加顺序即为消费顺序
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, "hashKey" + i, (message + i).getBytes(StandardCharsets.UTF_8));
            messages.add(msg);
        }
        // 发送消息
        try {
            producer.start();

            // 发送方式1：将消息发到指定的MessageQueue
            // fetchPublishMessageQueues()中传入的topic必须和Message设置的topic一致，否则发送消息的时候会报错
            List<MessageQueue> messageQueues = producer.fetchPublishMessageQueues(MQTopic.ZEA_TOPIC);
            // 根据需求获取一个MessageQueue
            MessageQueue msgQueue = messageQueues.get(0);
            // 该方法会将消息全部发送至 msgQueue 0，会造成负载不均衡
            producer.send(messages, msgQueue);

            // 发送方式2：传入MessageQueue选择器并传入arg来选择具体的MessageQueue，实现MessageQueue负载均衡
            // 传入的多个消息的arg的值必须一致，否则不会分发到同一个队列中，也就无法实现顺序消费
            for (Message msg : messages) {
                producer.send(msg, new SelectMessageQueueByHash(), "hashKey");
            }

            producer.shutdown();
        } catch (Exception e) {
            log.error("send order message error: {}", e.getMessage(), e);
        }
        return ResultUtil.success();
    }

    @GetMapping("sendTransactionMessage")
    public Result<?> sendTransactionMessage(@RequestParam(name = "message") String message) {
        RocketMqConfig rocketMqConfig = SpringBeanUtil.getBean(RocketMqConfig.class);
        if (rocketMqConfig == null) {
            throw new MyException(ErrorCode.SYSTEM_EXCEPTION, "rocketMqConfig没有配置！");
        }
        String producerGroup = rocketMqConfig.getProducerGroup();
        String nameSrvAddr = rocketMqConfig.getNameServer();
        log.info("producerGroup: {}, namesrvAddr: {}", producerGroup, nameSrvAddr);

        // 构建事务类型生产者
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameSrvAddr);
        // 构建消息:添加顺序即为消费顺序
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message(MQTopic.ZEA_TOPIC, MQTag.SYNC_TAG, "hashKey" + i, (message + i).getBytes(StandardCharsets.UTF_8));
            messages.add(msg);
        }
        // 发送消息
        try {
            producer.start();

            // 发送方式2：传入MessageQueue选择器并传入arg来选择具体的MessageQueue，实现MessageQueue负载均衡
            // 传入的多个消息的arg的值必须一致，否则不会分发到同一个队列中，也就无法实现顺序消费
            for (Message msg : messages) {
                producer.send(msg, new SelectMessageQueueByHash(), "hashKey");
            }

            producer.shutdown();
        } catch (Exception e) {
            log.error("send order message error: {}", e.getMessage(), e);
        }

        // 添加用户
        User user = new User();
        user.setUserName("zea");
        feignUserService.addUser(user);
        int x = 1/0;
        return ResultUtil.success();
    }

}
