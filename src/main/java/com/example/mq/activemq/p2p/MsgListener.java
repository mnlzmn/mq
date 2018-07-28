package com.example.mq.activemq.p2p;

import javax.jms.*;
import java.util.Enumeration;

/**
 * Created by Administrator on 2018/7/22.
 */
public class MsgListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("*********" + textMessage.getText());
            } else if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                System.out.println("------Received MapMessage------");
                System.out.println(mapMessage.getLong("long"));
                System.out.println(mapMessage.getBoolean("boolean"));
                System.out.println(mapMessage.getShort("short"));
                System.out.println(mapMessage.getString("MapMessage"));
                System.out.println("------Received MapMessage for while------");
                Enumeration enumer = mapMessage.getMapNames();
                while (enumer.hasMoreElements()) {
                    Object obj = enumer.nextElement();
                    System.out.println(mapMessage.getObject(obj.toString()));
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
