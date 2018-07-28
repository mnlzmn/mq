package com.example.mq.spring;

import com.example.mq.activemq.spring.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

import static org.junit.Assert.*;

/**
 * spring+activemq
 * Created by Administrator on 2018/7/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJmsApplicationTests {
    @Autowired
    private Producer producer;

    @Test
    public void sendMessage() throws Exception {
        Destination destination = new ActiveMQQueue("mytest");

        for (int i = 0; i < 10; i++) {
            producer.sendMessage(destination, "test !!!");
        }
    }

}