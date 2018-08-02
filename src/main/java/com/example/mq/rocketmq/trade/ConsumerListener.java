package com.example.mq.rocketmq.trade;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lrw360.framework.mq.GlobalConfig;
import com.lrw360.soa.finance.api.ApiBalance;
import com.lrw360.soa.mq.abnormal.service.AbnormalService;
import com.lrw360.soa.trade.member.api.TbMemberRecommendProductService;
import com.lrw360.soa.trade.member.api.TbMemberRelationService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */
@Component
public class ConsumerListener {

    private DefaultMQPushConsumer consumer;

    private final String NamesrvAddr = "192.168.1.121:9876";
    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    @Autowired
    private AbnormalService abnormalService;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private TbMemberRecommendProductService tbMemberRecommendProductService;

    @Autowired
    private TbMemberRelationService tbMemberRelationService;

    @Autowired
    private PublishMessage publishMessage;

    @Reference
    private ApiBalance apiBalance;


    public ConsumerListener(@Value("${mq.rocket.consumer}") String consumerGroup) {
        try {
            consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(NamesrvAddr);
            //广播模式
//           consumer.setMessageModel(MessageModel.BROADCASTING);

            //这里设置的是一个consumer的消费策略
            //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
            //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
            //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //设置consumer所订阅的Topic和Tag，*代表全部
            //监听多个topic
            consumer.subscribe(GlobalConfig.PRO_TAG, "*");
            consumer.subscribe(GlobalConfig.TRADE_TAG,"*");
            consumer.subscribe(GlobalConfig.FINANCE_TAG, "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void consum() {

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    String topic = messageExt.getTopic();
                    String tags = messageExt.getTags();
                    String msg = new String(messageExt.getBody());
                    logger.info("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + msg + ",	topic:" + topic + ", tags:" + tags);
//                    if (topic.equals(GlobalConfig.PRO_TAG)) {
//                        // 添加用户产品
//                        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
//                        pool.execute(new AddProductTask(tbMemberRelationService, tbMemberRecommendProductService, msg));
//
//                    } else if (topic.equals(GlobalConfig.TRADE_TAG)) {
//                        // 交易的数据信息
//                        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
//                        pool.execute(new ShareBenefitTask(tbMemberRelationService, tbMemberRecommendProductService, publishMessage, msg));
//
//                    } else if (topic.equals(GlobalConfig.FINANCE_TAG)) {
//                        // 用户收益的消息
//                        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
//                        pool.execute(new UpdateBalanceTask(abnormalService, jedisConnectionFactory, apiBalance, msg));
//                    }
                }
                //返回消费状态
                //CONSUME_SUCCESS 消费成功
                //RECONSUME_LATER 消费失败，需要稍后重新消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            logger.error("Consumer message failed. ");
        }

    }


}
