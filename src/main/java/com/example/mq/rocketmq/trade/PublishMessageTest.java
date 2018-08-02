package com.example.mq.rocketmq.trade;

import com.lrw360.framework.mq.MQTagsType;
import com.lrw360.framework.mq.ResultBean;
import com.lrw360.framework.mq.SendMessage;
import com.lrw360.soa.trade.TestBase;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Administrator on 2018/7/30.
 */
public class PublishMessageTest extends TestBase {
    @Autowired
    private PublishMessage publishMessage;

    private int i=2;

    @Test
    public void sendTest() {
        SendMessage sendMessage=new SendMessage();
        sendMessage.setData("这是第"+i+"条测试数据");
        sendMessage.setTags(MQTagsType.PRO_TAG);
        ResultBean<SendResult> sendResultResultBean = publishMessage.sendMsg(sendMessage);
        System.out.println(sendResultResultBean);
    }

}