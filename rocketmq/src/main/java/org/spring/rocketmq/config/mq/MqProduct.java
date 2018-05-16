package org.spring.rocketmq.config.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MqProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQProducer.class);
    @Value("${spring.application.name}")
    private String producerGroup;
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${apache.rocketmq.product.retryTimesWhenSend}")
    private int retryTimesWhenSend;
    @Value("${apache.rocketmq.product.sendMsgTimeout}")
    private int sendMsgTimeout;
    private DefaultMQProducer producer = null;

    /**
     * 初始化
     */
    @PostConstruct
    public void start() {
        try {
            LOGGER.info("MQ：启动生产者");
            producer = new DefaultMQProducer(producerGroup);
            producer.setRetryTimesWhenSendFailed(retryTimesWhenSend);
            producer.setSendMsgTimeout(sendMsgTimeout);
            producer.setNamesrvAddr(namesrvAddr);
            producer.start();
        } catch (Exception e) {
            LOGGER.error("MQ 启动失败{}",e.getMessage(),e);
        //    LOGGER.error("MQ：启动生产者失败：{}-{}", e.getResponseCode(), e.getErrorMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 发送消息 异步发送
     *
     * @param messageDto 消息内容
     */
    public Boolean sendMessageAsync(MessageDto messageDto) {
        boolean flag=true;
        try {
            byte[] messageBody = JSON.toJSONString(messageDto).getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message mqMsg = new Message(messageDto.getTopic(), messageDto.getTag(), messageDto.getId(), messageBody);
            producer.send(mqMsg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("MQ: 生产者发送消息 {}", sendResult);
                }
                @Override
                public void onException(Throwable throwable) {
                    LOGGER.error(throwable.getMessage(), throwable);
                }
            });
        } catch (Exception e) {
            LOGGER.error("调用sendMessageAsync方法发生异常，异常信息为", e);
            flag=false;
        }
        return flag;
    }
    /**

     * 发送消息 同步发送
     *
     * @param messageDto 消息内容
     */
    public Boolean sendMessageSync(MessageDto messageDto){
        boolean flag=true;
        SendResult result=new SendResult();
        try{
            byte[] messageBody = JSON.toJSONString(messageDto).getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message mqMsg = new Message(messageDto.getTopic(), messageDto.getTag(), messageDto.getId(), messageBody);
            result=producer.send(mqMsg);
            if(!"SEND_OK".equals(result.getSendStatus().name())){
                flag=false;
            }
        }catch (Exception e){
            LOGGER.error("调用sendMessageSync方法发生异常，异常信息为", e);
        }
        return flag;
    }
    /**
     * 发送消息 同步发送
     *
     * @param messageDto 消息内容
     */
    public Boolean sendMessageSyncTimeOut(MessageDto messageDto,long timeout){
        boolean flag=true;
        SendResult result=null;
        try{
            byte[] messageBody = JSON.toJSONString(messageDto).getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message mqMsg = new Message(messageDto.getTopic(), messageDto.getTag(), messageDto.getId(), messageBody);
            result=producer.send(mqMsg, timeout);
            if(!"SEND_OK".equals(result.getSendStatus().name())){
                flag=false;
            }
        }catch (Exception e){
            LOGGER.error("调用sendMessage方法异常，异常信息为",e);
        }
        return flag;
    }
    @PreDestroy
    public void stop() {
        if (producer != null) {
            producer.shutdown();
            LOGGER.info("MQ：关闭生产者");
        }
    }
}
