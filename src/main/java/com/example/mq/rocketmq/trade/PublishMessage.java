package com.example.mq.rocketmq.trade;

import com.lrw360.framework.mq.ResultBean;
import com.lrw360.framework.mq.SendMessage;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/30.
 */
@Component
public class PublishMessage {

//    @Value("${mq.rocket.topic}")
//    private String topic;

    private static final String NamesrvAddr = "192.168.1.121:9876";
    private static final Logger logger = LoggerFactory.getLogger(PublishMessage.class);

    private DefaultMQProducer producer;

    public PublishMessage(@Value("${mq.rocket.producer.id}") String producerGroup) {
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(NamesrvAddr);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer start failed");
            return;
        }
    }

    /**
     * 发送消息
     *
     * @param sendMessage
     * @return
     */
    public ResultBean<SendResult> sendMsg(SendMessage sendMessage) {
        ResultBean<SendResult> resultBean = new ResultBean();
        try {
            Message msg = new Message(sendMessage.getTags().getTags(), sendMessage.getTags().getTags(), sendMessage.getData().getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            if (sendResult != null) {
                resultBean.setCode(Integer.valueOf(200));
                resultBean.setData(sendResult);
                logger.info(new Date() + " Send mq message success. Topic is:" + sendMessage.getTags().getTags() + " msgId is: " + sendResult.getMsgId());
            }
        } catch (Exception e) {
            resultBean.setCode(Integer.valueOf(500));
            logger.error(" Send mq message failed. Topic is:" + sendMessage.getTags().getTags());
            return null;
        } finally {
            producer.shutdown();
        }
        return resultBean;
    }
}
