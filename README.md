# mq
rocketmq代码示例

## pom文件：
```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>4.0.0-incubating</version>
</dependency>
```
## producer:
```java
@Component
public class PublishMessage {

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
```
## consumer
```java
@Component
public class ConsumerListener {

    private DefaultMQPushConsumer consumer;

    private final String NamesrvAddr = "192.168.1.121:9876";
    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

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
```

