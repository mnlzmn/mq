package com.example.mq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Administrator on 2018/7/23.
 */
public class Counsumer2 {
    public final String SELECT_1 = "name='1' and age=20";
    public final String SELECT_2 = "name='2' and age=30";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private Destination destination;

    public Counsumer2() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://0.0.0.0:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("topic");
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

    class MsgListener implements MessageListener {

        @Override
        public void onMessage(Message message) {
            try {
                if (message instanceof TextMessage) {
                    System.out.println("customer2收到消息");
                } else if (message instanceof MapMessage) {
                    MapMessage mapMessage = (MapMessage) message;
                    mapMessage.getString("key");
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Counsumer2().receiver();
    }
}
