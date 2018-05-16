package org.spring.rocketmq.config.mq;
import java.util.Date;

public class MessageDto {

    /** 消息唯一编码 **/
    private String id;
    /** 消息实体 **/
    private Object object;
    /** 日期 **/
    private Date date;
    /** tag标签**/
    private String tag;
    /** topic **/
    private String topic;
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Date getDate() {
        if(date==null){
            date=new Date();
        }
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
