package org.spring.rocketmq.config.mq;

public interface MessageProcessor  {

    /**
     * 处理消息的接口
     * @param jsonMsg
     * @return
     */
    public  boolean handleMessage(String jsonMsg);
}
