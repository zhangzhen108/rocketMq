package org.spring.rocketmq.config.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;

@Component
public class MqConsumer implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqConsumer.class);
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${spring.application.name}")
    private String consumerGroup;
    @Value("${apache.rocketmq.cosumer.maxReconsumeTimes}")
    private int maxReconsumeTimes;
    @Value("${apache.rocketmq.cosumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${apache.rocketmq.cosumer.consumeThreadMax}")
    private int consumeThreadMax;
    private List<DefaultMQPushConsumer> consumerList = new ArrayList<DefaultMQPushConsumer>();
    /**
     * mq监听类 及与topic和tag关系
     **/
    private List<TopicsTagsObject> topicsTagsObjectList = new ArrayList<TopicsTagsObject>();

    /**
     * 初始化监听类与topic和tag的关系
     **/
    private void init() {
        try {
            for (int i = 0; i < topicsTagsObjectList.size(); i++) {
                TopicsTagsObject topicsTagsObject = topicsTagsObjectList.get(i);
                LOGGER.info("MQ：启动消费者" + i + "topic=" + topicsTagsObject.getTopic().getCode() + "tag==" + topicsTagsObject.getTags().getCode());
                /** 开启消费者 **/
                this.start(topicsTagsObject);
            }
        } catch (Exception e) {
            LOGGER.error("MQ：启动消费者失败：{}", e);
        }

    }
    public void start(TopicsTagsObject topicsTagsObject) throws Exception{

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(topicsTagsObject.getConsumerGroup().name());
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setMaxReconsumeTimes(maxReconsumeTimes);
        // 从消息队列头开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 集群消费模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 订阅主题
        consumer.subscribe(topicsTagsObject.getTopic().getCode(), topicsTagsObject.getTags().getCode());
        // 注册消息监听器
        MessageListener messageListener = new MessageListener();
        MessageProcessor messageProcessor = (MessageProcessor) topicsTagsObject.getObject();
        messageListener.setMessageProcessor(messageProcessor);
        consumer.registerMessageListener(messageListener);
        // 启动消费端
        consumer.start();
        consumerList.add(consumer);
    }

    @PreDestroy
    public void stop() {
        if (consumerList != null) {
            for (int i = 0; i < consumerList.size(); i++) {
                consumerList.get(i).shutdown();
                LOGGER.info("MQ：关闭消费者" + i);
            }

        }
    }

    /**
     * 初始化监听类与topic和tag的关系
     **/
    private void init(ApplicationContext applicationContext) {
        Map<String, MessageProcessor> map = applicationContext.getBeansOfType(MessageProcessor.class);
        /**
         * key 为group
         * value 为topic+tag(订阅关系)
         * map用来校验
         * 同一个组中的订阅关系必须相同
         */
        Map<String, String> checkMap = new HashMap<String, String>();
        for (Object o : map.keySet()) {
            MessageProcessor messageProcessor = map.get(o);
            MqListener annotation = messageProcessor.getClass().getAnnotation(MqListener.class);
            /** 订阅关系校验**/
            /*if(!this.checkSubscribe(annotation,checkMap)){
                continue;
            }*/
            /** 符合要求，加入待开启订阅中 **/
            TopicsTagsObject topicsTagsObject = new TopicsTagsObject();
            topicsTagsObject.setObject(messageProcessor);
            topicsTagsObject.setTags(annotation.tag());
            topicsTagsObject.setConsumerGroup(annotation.consumerGroup());
            topicsTagsObject.setTopic(annotation.topic());
            topicsTagsObjectList.add(topicsTagsObject);
        }
    }

    public boolean checkSubscribe(MqListener annotation,Map<String,String> checkMap) {
        if (annotation == null) {
            return false;
        }
        /** currentTopicTag 需检验的当前订阅 **/
        String currentTopicTag = annotation.topic().getCode() + annotation.tag().getCode();
        if (checkMap.containsKey(annotation.consumerGroup().getCode())) {
            /** topicTag 同组内已符合要求的订阅 **/
            String topicTag = checkMap.get(annotation.consumerGroup().getCode());
            if (!topicTag.equals(currentTopicTag)) {
                LOGGER.warn("MQ：topic为{}、tag为{}订阅失败，原因：同一订阅组中的订阅关系必须相同", annotation.topic().name(), annotation.tag().name());
                return false;
            }
            return true;
        }else {
            checkMap.put(annotation.consumerGroup().getCode(), currentTopicTag);
            return true;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /** 初始化监听类与topic和tag的关系 **/
        this.init(applicationContext);
        /** 初始化consumer**/
        this.init();
    }
}
