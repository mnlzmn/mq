package com.example.mq.activemq.p2p;

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
            Destination destination = session.createQueue("first");
//            MapMessage msg1 = session.createMapMessage();
//            msg1.setString("name", "1");
//            msg1.setInt("age", 20);
//
//            MapMessage msg2 = session.createMapMessage();
//            msg2.setStringProperty("name", "2");
//            msg2.setIntProperty("age", 30);
//
//            messageProducer.send(destination, msg1);
//            messageProducer.send(destination, msg2);

//            TextMessage textMessage = session.createTextMessage("消息");
//            messageProducer.send(destination, textMessage);

            MapMessage msg = session.createMapMessage();
            msg.setBoolean("boolean", true);
            msg.setShort("short", (short) 0);
            msg.setLong("long", 123456);
            msg.setString("MapMessage", "ActiveMQ Map Message!");
            messageProducer.send(destination, msg);


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Producer().send();
    }
}
