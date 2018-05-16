package org.spring.rocketmq.config.mq.mqListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.rocketmq.config.mq.MessageProcessor;
import org.spring.rocketmq.config.mq.MqListener;
import org.spring.rocketmq.config.mq.RocketMqEnum;
import org.springframework.stereotype.Service;

@Service
@MqListener(tag = RocketMqEnum.TagsEnum.DIGITALSTORE_MESSAGE_TAG, topic = RocketMqEnum.TopicEnum.DIGITALSTORE_MESSAGE_TOPIC, consumerGroup = RocketMqEnum.GroupEnum.DIGITALSTORE_MESSAGE_GROUP)
public class MessagePushProcessorListenterDemo implements MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(MessagePushProcessorListenterDemo.class);

    public boolean handleMessage(String jsonMsg) {
        logger.info("MessagePushProcessorListenterDemo 消费的消息 : {}", jsonMsg);
        return true;
    }


}
