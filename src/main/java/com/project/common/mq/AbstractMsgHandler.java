package com.project.common.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 消息处理基类
 * ClassName: AbstractMsgHandler <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月10日
 * @version 1.0
 */
public abstract class AbstractMsgHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public abstract ConsumeConcurrentlyStatus handler(Message paramMessage);

    public void logInfo(Message message) {
        if (this.logger.isInfoEnabled())
            if ((message instanceof MessageExt)) {
                MessageExt ext = (MessageExt) message;
                this.logger.info("{} @ msg : {} @ " + ext.getMsgId(), message.getTopic() + "#" + message.getTags(), new String(message.getBody()));
            } else {
                this.logger.info("{} @ msg : {}", message.getTopic() + "#" + message.getTags(), new String(message.getBody()));
            }
    }
}