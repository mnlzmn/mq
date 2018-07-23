package com.example.mq.p2p;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者
 * <p>
 * Created by Administrator on 2018/7/21.
 */
public class Counsumer {

    public final String SELECT_1 = "name='1'";
    public final String SELECT_2 = "name='2'";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private Destination destination;

    public Counsumer() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://0.0.0.0:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("first");
            //加入条件
            messageConsumer = session.createConsumer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiver() {
        try {
            messageConsumer.setMessageListener(new MsgListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Counsumer().receiver();
    }
}
