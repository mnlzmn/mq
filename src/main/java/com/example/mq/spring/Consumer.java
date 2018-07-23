package com.example.mq.spring;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/23.
 */
@Component
public class Consumer {

    /**
     * 使用JmsListener配置消费者监听的队列
     * 接收到消息后在发送到out.queue队列中
     *
     * @param text 接收到的消息
     */
    @JmsListener(destination = "mytest")
    @SendTo("out.queue")
    public String receiveQueue(String text) {
        System.out.println("Consumer收到的报文为:" + text);
        return text;
    }
}
