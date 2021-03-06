package org.spring.rocketmq.config.mq;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.TYPE})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中
public @interface MqListener {

    RocketMqEnum.GroupEnum consumerGroup();

    RocketMqEnum.TopicEnum topic();
    /**
     * tag
     * @return
     */
    RocketMqEnum.TagsEnum tag();
}
