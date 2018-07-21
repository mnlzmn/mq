package com.example.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 生产者
 * <p>
 * Created by Administrator on 2018/7/21.
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        //建立ConnectionFactory工厂对象，用户名、密码、地址均使用默认
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://0.0.0.0:61616");

        //创建Connection连接，并开启连接
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //通过connection创建session会话，用于接收对象，参数一为是否启用事物，参数二为签收模式，设置为自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //通过session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费信息来源的对象，在PTP模式中，Destination被称作Queue（队列）
        Destination destination = session.createQueue("FirstQueue");


        //通过session对象创建创建消息的发送和接受对象(MessageProducer/MessageConsumer),参数指定Destination对象;
        MessageProducer producer = session.createProducer(null);

        for (int i = 0; i < 10; i++) {
            TextMessage textMessage = session.createTextMessage("这是第" + i + "条消息");
            producer.send(destination, textMessage);

            Thread.sleep(1000);
        }

        if (connection != null) {
            connection.close();
        }

    }
}
