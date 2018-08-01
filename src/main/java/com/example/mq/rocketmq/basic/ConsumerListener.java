package com.example.mq.rocketmq.basic;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */
@Component
public class ConsumerListener {

    private String consumerGroup = "CID_LRW_DEV_SUBS";
    private String topic = "LRW_COM_TOPIC";

    private final String NamesrvAddr = "192.168.1.121:9876";
    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    DefaultMQPushConsumer consumer;

    private static final String ip = "47.105.47.171:9876";
    private static final String ip2 = "192.168.1.121:9876";

    public ConsumerListener() {
        try {
            consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(ip2);
            //广播模式,默认集群模式
            //consumer.setMessageModel(MessageModel.BROADCASTING);

            //这里设置的是一个consumer的消费策略
            //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
            //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
            //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //设置consumer所订阅的Topic和Tag，*代表全部的Tag
            consumer.subscribe(topic, "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void consum() {

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    String tag = messageExt.getTags();
                    String msg = new String(messageExt.getBody());
                    logger.info("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + msg + ",	tag:" + tag);
                    System.out.println("*********************************1");
                }
                //返回消费状态
                //CONSUME_SUCCESS 消费成功
                //RECONSUME_LATER 消费失败，需要稍后重新消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        System.out.println("*********************************start");
        try {
            consumer.start();
        } catch (MQClientException e) {
            logger.error("Consumer message failed. Topic is:" + topic);
        }

    }


}

