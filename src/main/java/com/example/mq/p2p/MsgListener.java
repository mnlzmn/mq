package com.example.mq.p2p;

import javax.jms.*;

/**
 * Created by Administrator on 2018/7/22.
 */
public class MsgListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                textMessage.getText();
            } else if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                mapMessage.getString("key");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
