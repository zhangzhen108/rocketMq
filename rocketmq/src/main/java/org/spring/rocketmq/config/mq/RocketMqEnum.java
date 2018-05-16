package org.spring.rocketmq.config.mq;

/**
 * mq枚举类
 */
public interface RocketMqEnum {

    enum GroupEnum {
        DIGITALSTORE_MESSAGE_GROUP1("digitalstore_message_group1"),
        DIGITALSTORE_MESSAGE_GROUP("digitalstore_message_group");
        private String code;

        GroupEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    enum TopicEnum {
        DIGITALSTORE_MESSAGE_TOPIC1("digitalstore_message_topic1"),
        DIGITALSTORE_MESSAGE_TOPIC("digitalstore_message_topic");
        private String code;

        TopicEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    enum TagsEnum {
        DIGITALSTORE_MESSAGE_TAG1("digitalstore_message_tag1"),
        DIGITALSTORE_MESSAGE_TAG("digitalstore_message_tag");
        private String code;

        TagsEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
