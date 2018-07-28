package com.example.mq.activemq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 生产者
 * <p>
 * Created by Administrator on 2018/7/22.
 */
public class Producer {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public Producer() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://0.0.0.0:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return this.session;
    }

    public void send() {
        try {
            Destination destination = session.createTopic("topic");
            TextMessage textMessage = session.createTextMessage("发送消息");
            messageProducer.send(destination, textMessage);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Producer().send();
    }
}
