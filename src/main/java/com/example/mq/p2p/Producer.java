package com.example.mq.p2p;

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
            Destination destination = session.createQueue("First");
            MapMessage msg1 = session.createMapMessage();
            msg1.setStringProperty("name", "1");
            msg1.setIntProperty("age", 20);

            MapMessage msg2 = session.createMapMessage();
            msg2.setStringProperty("name", "2");
            msg2.setIntProperty("age", 30);

            messageProducer.send(destination, msg1);
            messageProducer.send(destination, msg2);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Producer().send();
    }
}
