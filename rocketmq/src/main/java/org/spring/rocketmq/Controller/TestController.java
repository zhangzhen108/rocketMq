package org.spring.rocketmq.Controller;


import org.spring.rocketmq.config.mq.MessageDto;
import org.spring.rocketmq.config.mq.MqProduct;
import org.spring.rocketmq.config.mq.RocketMqEnum;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@EnableAutoConfiguration
public class TestController {

    @Resource
    MqProduct mqProduct;

    @RequestMapping("/aa")
    public void testProduct(){
        MessageDto messageDto=new MessageDto();
        messageDto.setDate(new Date());
        messageDto.setId(UUID.randomUUID().toString());
        messageDto.setObject("zhangzhen111111");
        messageDto.setTopic(RocketMqEnum.TopicEnum.DIGITALSTORE_MESSAGE_TOPIC.getCode());
        messageDto.setTag(RocketMqEnum.TagsEnum.DIGITALSTORE_MESSAGE_TAG.getCode());
        mqProduct.sendMessageSync(messageDto);
        MessageDto msg=new MessageDto();
        msg.setDate(new Date());
        msg.setId(UUID.randomUUID().toString());
        messageDto.setTopic(RocketMqEnum.TopicEnum.DIGITALSTORE_MESSAGE_TOPIC1.getCode());
        messageDto.setTag(RocketMqEnum.TagsEnum.DIGITALSTORE_MESSAGE_TAG1.getCode());
        msg.setObject("zhangzhen2222");
        mqProduct.sendMessageSync(msg);
    }
}
