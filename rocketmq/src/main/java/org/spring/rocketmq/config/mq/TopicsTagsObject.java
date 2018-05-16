package org.spring.rocketmq.config.mq;

/**
 * 初始化mq监听类
 */
public class TopicsTagsObject {
    private RocketMqEnum.TopicEnum topic;
    private RocketMqEnum.TagsEnum tags;
    private Object object;
    private RocketMqEnum.GroupEnum consumerGroup;

    public RocketMqEnum.TopicEnum getTopic() {
        return topic;
    }

    public void setTopic(RocketMqEnum.TopicEnum topic) {
        this.topic = topic;
    }

    public RocketMqEnum.TagsEnum getTags() {
        return tags;
    }

    public void setTags(RocketMqEnum.TagsEnum tags) {
        this.tags = tags;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public RocketMqEnum.GroupEnum getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(RocketMqEnum.GroupEnum consumerGroup) {
        this.consumerGroup = consumerGroup;
    }
}
