package com.example.mq.rocketmq.basic;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/1.
 */
public class producer {

    private DefaultMQProducer producer;
    private static final String NamesrvAddr = "192.168.1.121:9876";
    private String topic;
    private String producerGroup;

    public producer() {
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(NamesrvAddr);
        try {
            producer.start();
        } catch (MQClientException e) {
            System.out.println("producer start failed");
            return;
        }
    }

    /**
     * 发送消息
     *
     * @param tag 标签
     * @param content 发送内容
     */
    public void sendMsg(String tag,String content) {
        try {
            Message msg = new Message(topic,tag, content.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            if (sendResult != null) {
                System.out.println(new Date() + " Send mq message success. Topic is:" + this.topic + " msgId is: " + sendResult.getMsgId());
            }
        } catch (Exception e) {
            System.out.println("Send mq message failed. Topic is:" + topic);
        } finally {
            producer.shutdown();
        }
    }
}
